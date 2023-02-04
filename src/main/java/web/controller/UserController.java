package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping
    public String printUsers(ModelMap modelMap) {
        List<User> users = userService.getUsers();

        modelMap.addAttribute("users", users);

        return "index";
    }


    @GetMapping("/create")
    public String printCreateForm(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("isCreate", true);

        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, ModelMap modelMap) {
        if (!bindingResult.hasErrors())
            userService.createUser(user);

        modelMap.addAttribute("isCreate", true);
        modelMap.addAttribute("user", new User());

        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String printEditForm(@RequestParam(value = "id", required = false) Integer id, ModelMap modelMap) {
        String redirectNotExistsTo = "/users";
        User user = id == null ? null : userService.getUserById(id);

        if (user == null) return "redirect:" + redirectNotExistsTo;

        modelMap.addAttribute("isCreate", false);
        modelMap.addAttribute("user", user);

        return "create";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam("id") Integer id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (!bindingResult.hasErrors())
            userService.updateUser(user);

        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String printDeleteUser(@RequestParam(value = "id") Integer id) {
        userService.deleteUser(id);

        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Integer id) {
        userService.deleteUser(id);

        return "redirect:/users";
    }

}
