package com.huawei.hostingtrial.web;

import com.huawei.hostingtrial.domain.form.DeveloperForm;
import com.huawei.hostingtrial.domain.form.UserRegisterForm;
import com.huawei.hostingtrial.domain.User;
import com.huawei.hostingtrial.domain.UserAccountTypeEnum;
import com.huawei.hostingtrial.repository.UserRepository;
import com.huawei.hostingtrial.service.PhoneService;
import com.huawei.hostingtrial.service.UserService;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/users")
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authMgr;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    
    @Autowired
    PhoneService phoneService;

    @InitBinder("account")
    public void initAccountBinder(WebDataBinder binder){
    	binder.setAllowedFields(new String[] {
    		"username", "firstName", "lastName", "password", "confirmPassword", "email", "imsUsername", "imsPassword", "description"
    	});
    }
    
    @InitBinder("developer")
    public void initDeveloperBinder(WebDataBinder binder){
    	binder.setAllowedFields(new String[] {
    		"description","totalPhoneNums"
    	});
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@ModelAttribute("account") @Valid UserRegisterForm form, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    	
    	if (bindingResult.hasErrors()) {
    		uiModel.addAttribute("account", form);
            return "users/create";
        }
        String password_plain = form.getPassword();
        uiModel.asMap().clear();
        User user = toUserAccount(form);
        userService.saveDefaultUserAndPermission(user);
        //auto login user with web identity after registration
        Authentication authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), password_plain);
        Authentication authResult = authMgr.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        return "redirect:/users/username/" + encodeUrlPathSegment(user.getUsername(), httpServletRequest);
    }

    @RequestMapping(value = "/developer",method = RequestMethod.GET, produces = "text/html")
    @PreAuthorize("hasRole('ROLE_USER')")    
    public String developerForm(Model uiModel, HttpServletRequest httpServletRequest, Principal currentUser) {
    	User developer = userService.getUser(currentUser); 
    	DeveloperForm form = new DeveloperForm();
    	form.setDescription(developer.getDescription());
    	form.setTotalPhoneNums(developer.getTotalPhoneNumbers());
        uiModel.addAttribute("developer", form);
        return "users/developer";
    }
    
    @RequestMapping(value = "/developer",method = RequestMethod.POST, produces = "text/html")
    @PreAuthorize("hasRole('ROLE_USER')")  
    public String becomeDeveloper(@ModelAttribute("developer") @Valid DeveloperForm form, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, Principal currentUser) {
    	User developer = userService.getUser(currentUser);  
    	if (bindingResult.hasErrors()) {
    		uiModel.addAttribute("developer", form);
            return "users/developer";
        }
    	
    	//update phone numbers, could be newly allocate or change existing numbers
    	try {
    		phoneService.changePhoneNums(developer, form.getTotalPhoneNums());
    	} catch(RuntimeException e){
    		redirectAttributes.addFlashAttribute("errorMessage", "Error allocate phone numbers. The Error message is "+e.getMessage());
    		return "redirect:/users/developer";
    	}
    	
    	//add developer role if necessary
    	if(!developer.getIsDeveloper()){
    		developer.setIsDeveloper(true);
        	userService.addUserAuthority(developer, "ROLE_DEVELOPER");   		
    	}
    	
    	//update developer account
    	developer.setDescription(form.getDescription());
    	developer.setTotalPhoneNumbers(form.getTotalPhoneNums());
    	
    	userRepository.save(developer);
    	
        return "redirect:/users/username/" + encodeUrlPathSegment(developer.getUsername(), httpServletRequest);
    }

    
    @RequestMapping(value = "/new", produces = "text/html")
    public String createForm(Model uiModel) {
        //populateEditForm(uiModel, new User());
        uiModel.addAttribute("account", new UserRegisterForm());
        return "users/create";
    }

    //access from username
    @RequestMapping(value = "/username/{username}", produces = "text/html")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #username")    
    public String showByUsername(@PathVariable("username") String username, Model uiModel, Principal currentUser) {
    	User user = userService.getUser(username);
    	if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();  
        uiModel.addAttribute("user", user);
        uiModel.addAttribute("itemId", user.getId());
        return "users/show";
    }

    
    @RequestMapping(produces = "text/html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, Principal currentUser) {
        //check is user is admin
        if (!userService.isAdmin(currentUser)) return accessDeniedRedirect();
        
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("users", userRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) userRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("users", userRepository.findAll());
        }
        return "users/list";
    }

    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #username") 
    public String update(@Valid UserRegisterForm form, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest, Principal currentUser) {
    	User user = userRepository.findOne(Long.getLong(form.getUserId()));
    	if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();
    	    	
    	if (bindingResult.hasErrors()) {
            uiModel.addAttribute("account", form);
            return "users/create";
        }
        
        //check password match
        String password_plain = form.getPassword();
        String hashedPassword = new BCryptPasswordEncoder().encode(password_plain);
        if (hashedPassword != user.getPassword()) {
            uiModel.addAttribute("account", form);
            //flash message
            return "users/create"; //password does not match
        }
        uiModel.asMap().clear();
        //check if need to re-login the user
        boolean needToLogin = false;
        if (form.getUsername() != user.getUsername()) needToLogin = true;
        //update user account from form
        mergeUserAccount(form, user);
        userRepository.save(user);
        //login user is username changes
        if (needToLogin) {
            Authentication authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), password_plain);
            Authentication authResult = authMgr.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        return "redirect:/users/username/" + encodeUrlPathSegment(user.getUsername(), httpServletRequest);
    }

    @RequestMapping(value = "/username/{username}/edit", produces = "text/html")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #username") 
    public String updateForm(@PathVariable("username") String username, Model uiModel, Principal currentUser) {
        User user = userService.getUser(username);
        if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();

        UserRegisterForm form = populateUserEditForm(user);
        uiModel.addAttribute("account", form);
        return "users/create";
    }
    
    @RequestMapping(value = "/username/{username}", method = RequestMethod.DELETE, produces = "text/html")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #username") 
    public String deleteFromUsername(@PathVariable("username") String username, Model uiModel, Principal currentUser) {
        User user = userService.getUser(username);
        if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();
     
        userRepository.delete(user);
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
    
    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
    
    private String accessDeniedRedirect() {
    	return "redirect:/";
    }

    
    private User toUserAccount(UserRegisterForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setPassword(form.getPassword());
        user.setDescription(form.getDescription());
        user.setEnabled(true); //should set it to false in production
        user.setImsUsername(form.getImsUsername());
        user.setImsPassword(form.getImsPassword());
        user.setAccountType(UserAccountTypeEnum.INDIVIDUAL);
        return user;
    }

    private void mergeUserAccount(UserRegisterForm form, User user) {
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        //update IMS credentials
        user.setImsUsername(form.getImsUsername());
        //if the ims password is empty, assume use does not update the ims password
        if (!form.getImsPassword().isEmpty()) {
        	user.setImsPassword(new BCryptPasswordEncoder().encode(form.getImsPassword()));
        }
    }

    private UserRegisterForm populateUserEditForm(User user) {
        UserRegisterForm form = new UserRegisterForm();
        form.setEmail(user.getEmail());
        form.setUsername(user.getUsername());
        form.setUserId(String.valueOf(user.getId()));
        form.setImsUsername(user.getImsUsername());
        return form;
    }
    
    
    
    
    

    //not used
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
    	return "redirect:/";
        //uiModel.addAttribute("user", userRepository.findOne(id));
        //uiModel.addAttribute("itemId", id);
        //return "users/show";
    }
    
    //not used
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel, Principal currentUser) {
        User user = userRepository.findOne(id);
        if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();
        userRepository.delete(user);
        //uiModel.asMap().clear();
        //uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        //uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
 
    //not used
    @RequestMapping(value = "/{id}/edit", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel, Principal currentUser) {
        User user = userRepository.findOne(id);
        if (!userService.crudPrivilege(user, currentUser)) return accessDeniedRedirect();

        UserRegisterForm form = populateUserEditForm(user);
        uiModel.addAttribute("account", form);
        return "users/create";
    }
    
    //not used
    void populateEditForm(Model uiModel, User user) {
        uiModel.addAttribute("user", user);
        //uiModel.addAttribute("applications", applicationRepository.findAll());
    }

}
