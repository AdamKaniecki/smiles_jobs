package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.RoleRepository;

@Controller
public class LoginController {
    private UserService userService;
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


//    @GetMapping("/loginUser")
//    public String loginUser(Model model, HttpSession session) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }

    // to działa jakby co
//    @PostMapping("/loginUser")
//    public String loginUser(Model model, HttpSession session) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
////            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
////            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//    @PostMapping("/loginUser")
//    public String loginUser(Model model, HttpSession session) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//}

//  to działa:
//    @PostMapping("/loginUser")
//    public String loginUser(@RequestParam("username") String username,Model model, HttpSession session) {
//
//        UserDetails user = (UserDetails) userService.findByUserName(username);
//
//        if (user != null
////                && user.getPassword().equals(password)
//                && user.getUsername().equals(username)){
////                && user.getRoles().contains(Role.CANDIDATE)) {
//            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect: /candidate_portal";
//        } else {
//            if (user != null
////                    && user.getPassword().equals(password)
//                    && user.getUsername().equals(username))
////                    && user.getRoles().contains(Role.COMPANY))
//            {
//                // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//                session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//                System.out.println("Zalogowano Firmę pomyślnie");
//                model.addAttribute("username", username);
//                return "redirect: /company_portal";
//            } else {
//                System.out.println("Nieprawidłowe dane logowania.");
//                return "login";
//            }
//        }
//    }

//    @PostMapping("/loginUser")
//    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {
//
//        User user = userService.findByUserName(username);
//
//        if (user != null
//                && user.getPassword().equals(password)
//                && user.getUserName().equals(username)
////                && user.getRoles().contains(Role.CANDIDATE)
//                )
//        {
//            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect: /candidate_portal";
//        } else {
//            if (user != null
//                    && user.getPassword().equals(password)
//                    && user.getUserName().equals(username)
////                    && user.getRoles().contains(Role.COMPANY)
//                    )
//            {
//                // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//                session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//                System.out.println("Zalogowano Firmę pomyślnie");
//                model.addAttribute("username", username);
//                return "redirect: /company_portal";
//            } else {
//                System.out.println("Nieprawidłowe dane logowania.");
//                return "login";
//            }
//
//        }
//    }

    //    @PostMapping("/loginUser")
//    public String loginUser(@RequestParam("username") String username,
//                            @RequestParam("password") String password,
//                            Model model,
//                            HttpSession session) {
//
//        User user = userService.findByUserName(username);
//
//        if (user != null && user.getPassword().equals(password)) {
//            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String loggedUsername = auth.getName();
//
//            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//                // Zalogowano Kandydata pomyślnie
//                System.out.println("Zalogowano Kandydata pomyślnie");
//                model.addAttribute("username", loggedUsername);
//                return "redirect:/candidate_portal";
//            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//                // Zalogowano Firmę pomyślnie
//                System.out.println("Zalogowano Firmę pomyślnie");
//                model.addAttribute("username", loggedUsername);
//                return "redirect:/company_portal";
//            } else {
//                System.out.println("Nieprawidłowe dane logowania.");
//                return "login";
//            }
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//}


    //        @PostMapping("/loginUser")
//    public String loginUser(Model model, HttpSession session) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
////            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
////            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
    @PostMapping("/loginUser")
    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {
        // Sprawdź, czy użytkownik o podanej nazwie istnieje w bazie danych
        User user = userService.findByUserName(username);
        if (user != null && user.getPassword().equals(password) && user.getUserName().equals(username)) {
            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
            System.out.println("Zalogowano pomyślnie");
            model.addAttribute("username", username);
            return "candidate_portal";
        } else {
            model.addAttribute("error", "Nieprawidłowe dane logowania.");
            return "login";
        }
    }
}




























//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }


