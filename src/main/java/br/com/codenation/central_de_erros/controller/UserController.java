package br.com.codenation.central_de_erros.controller;

import br.com.codenation.central_de_erros.assembler.UserResourceMapper;
import br.com.codenation.central_de_erros.entity.User;
import br.com.codenation.central_de_erros.resources.UserResource;
import br.com.codenation.central_de_erros.service.Impl.UserService;
import br.com.codenation.central_de_erros.validationGroups.OnCreate;
import br.com.codenation.central_de_erros.validationGroups.OnUpdate;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserResourceMapper userMapper;

    @PostMapping
    @ApiOperation("Sign Up new user.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Created new resource"),
            @ApiResponse(code = 400, message = "Missing not null field; wrong field name; wrong e-mail format; duplicated e-mail, trying to set id"),
    })
    @Validated(OnCreate.class)
    public ResponseEntity<UserResource> save(@Valid @RequestBody User newUser) throws URISyntaxException {
        UserResource resource = userMapper.map(userService.save(newUser));
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    @GetMapping
    @ApiOperation("Return logged user data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok. Return user data"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<UserResource> one(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userMapper.map((userService.getByEmail(auth.getName()))));
    }

    @PutMapping
    @ApiOperation("Modify Password and/or e-mail")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Password and/or e-mail modified"),
            @ApiResponse(code = 400, message = "Wrong e-mail format; duplicated e-mail, trying to set id"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @Validated(OnUpdate.class)
    public ResponseEntity<UserResource> update(@Valid @RequestBody User newUser) throws URISyntaxException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserResource resource = userMapper.map(userService.update(newUser, auth));
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
