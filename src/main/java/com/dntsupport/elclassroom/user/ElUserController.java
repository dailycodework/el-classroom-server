package com.dntsupport.elclassroom.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class ElUserController {

    private final ElUserService elUserService;

    @GetMapping
    public ResponseEntity<List<ElUser>> getUsers(){
        List<ElUser> allUser = elUserService.getUsers()
                .collect(Collectors.toList());
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<ElUser> getUser(@PathVariable("email") String email){
        ElUser theUser = elUserService.getUser(email);
        return new ResponseEntity<>(theUser, HttpStatus.OK);
    }

    @PostMapping("/disable/{email}")
    public int disableUser(@PathVariable("email")String email){
        return elUserService.disableUser(email);

    }

    @PostMapping("/enable/{email}")
    public int enableUser(@PathVariable("email")String email){
        return elUserService.enableUser(email);

    }

    @DeleteMapping("delete/{email}")
    public void deleteUser(@PathVariable("email") String email){
        elUserService.deleteUser(email);
    }
}
