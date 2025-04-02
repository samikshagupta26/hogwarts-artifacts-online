package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.api.endpoint.base-url}/users")
public class UserContoller {
    private final UserService userService;

    private final UserDtoToUserConverter userDtoToUserConverter;

    private final UserToUserDtoConverter userToUserDtoConverter;


    public UserContoller(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

}
