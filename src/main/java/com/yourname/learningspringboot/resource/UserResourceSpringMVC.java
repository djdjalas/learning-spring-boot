package com.yourname.learningspringboot.resource;

import com.yourname.learningspringboot.model.User;
import com.yourname.learningspringboot.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.QueryParam;

//@RestController
//@RequestMapping(
//    path = "/api/v1/users"
//)
public class UserResourceSpringMVC {

  private UserService userService;

  @Autowired
  public UserResourceSpringMVC(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<User> fetchUsers(@QueryParam("gender") String gender) {
    return userService.getAllUsers(Optional.ofNullable(gender));
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "{userUid}"
  )
  public ResponseEntity<?> fetchUser(@PathVariable("userUid") UUID userUid) {
    return userService.getUser(userUid).<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorMessage("user " + userUid + " was not found.")));
  }

  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Integer> insertNewUser(@RequestBody User user) {
    int result = userService.insertUser(user);
    return getIntegerResponseEntity(result);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Integer> updateUser(@RequestBody User user) {
    int result = userService.updateUser(user);
    return getIntegerResponseEntity(result);
  }

  @RequestMapping(
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      path = "{userUid}"
  )
  public ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid) {
    int result = userService.removeUser(userUid);
    return getIntegerResponseEntity(result);
  }

  private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
    if (result == 1) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }
}
