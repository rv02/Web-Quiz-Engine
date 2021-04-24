package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
class Controller {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CompletedRepository completedRepository;

    @Autowired
    public Controller(QuizRepository quizRepository, UserRepository userRepository, CompletedRepository completedRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.completedRepository = completedRepository;
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable("id") int id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException());
    }

    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getQuizzes(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return quizRepository.findAllQuizzesWithPagination(pageable);
    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    @ResponseBody
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz, Authentication authentication) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Optional<User> user = userRepository.findByEmail(authentication.getName());
            quiz.setUser(user.orElse(null));
        }
        return quizRepository.save(quiz);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve")
    @ResponseBody
    public Feedback solveQuiz(@PathVariable("id") int id,
                              @RequestBody() HashMap<String, int[]> answer,
                              Principal principal) {
        int[] arr1 = Arrays.copyOf(answer.get("answer"), answer.get("answer").length);
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException());
        int[] quizAns;
        if (quiz.getAnswer() == null) quizAns = new int[]{};
        else quizAns = Arrays.copyOf(quiz.getAnswer(), quiz.getAnswer().length);
        Arrays.sort(arr1);
        Arrays.sort(quizAns);
        if(Arrays.equals(arr1, quizAns)) {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("No authorized User"));
            Completed completed = new Completed(id, new Date(), user);
            completedRepository.save(completed);
            return new Feedback(true, "Congratulations, you're right!");
        } else {
            return new Feedback(false, "Wrong answer! Please, try again.");
        }
    }

    @GetMapping(path = "/api/quizzes/completed")
    public Page<Completed> getCompleted(Principal principal,
                                        @RequestParam(defaultValue = "0") int page) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No authorized User"));
        Pageable pageable = PageRequest.of(page, 10);
        return completedRepository.findAllCompletedPaginated(pageable, user.getId());
    }

    @PostMapping(path = "/api/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity deleteQuiz(@PathVariable("id") int id, Principal principal) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException());
        if (!quiz.getUser().getEmail().equals(principal.getName()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        quizRepository.delete(quiz);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}