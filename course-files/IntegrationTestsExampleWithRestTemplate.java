import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.OK;

import com.yourcompany.learningspringboot.model.User;
import com.yourcompany.learningspringboot.resource.UserResource;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void itShouldIntegrateAllMethodsInUserEndpoint() throws Exception {

    ParameterizedTypeReference<List<User>> personList = new ParameterizedTypeReference<List<User>>() {
    };

    // GET - ALL USERS
    ResponseEntity<List<User>> response = restTemplate.exchange("/api/v1/users", GET, null, personList);
    assertThat(response.getBody()).hasSize(0);
    assertThat(response.getStatusCode()).isEqualTo(OK);

    // POST - NEW USER
    User bareUser = new User(null, "", "", "", "", 0);
    HttpEntity<User> entity = new HttpEntity<>(bareUser, null);
    ResponseEntity<String> exchange = restTemplate.exchange("/api/v1/users", POST, entity, String.class);
    assertThat(exchange.getStatusCode()).isEqualTo(OK);

    // GET - ALL USERS
    response = restTemplate.exchange("/api/v1/users", GET, null, personList);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody().get(0)).isEqualToIgnoringNullFields(bareUser);

    // GET BY USER BY ID=1
    ResponseEntity<User> getUserByIdResponse = restTemplate
        .exchange("/api/v1/users/1", GET, null, User.class);
    assertThat(getUserByIdResponse.getStatusCode()).isEqualTo(OK);
    assertThat(getUserByIdResponse.getBody()).isEqualToIgnoringNullFields(bareUser);

    // PUT - UPDATE USER BY ID=1
    User userToUpdate =
        new User(1, "John", "Jones", "john.jones@gmail.com", "M", 22);
    entity = new HttpEntity<>(userToUpdate, null);
    ResponseEntity<User> updateUserByIdResponse = restTemplate
        .exchange("/api/v1/users", PUT, entity, User.class);
    assertThat(updateUserByIdResponse.getStatusCode()).isEqualTo(OK);

    // GET - USER BY ID=1
    getUserByIdResponse = restTemplate.exchange("/api/v1/users/1", GET, null, User.class);
    assertThat(getUserByIdResponse.getStatusCode()).isEqualTo(OK);
    assertThat(getUserByIdResponse.getBody()).isEqualToComparingFieldByField(userToUpdate);

    // POST - INSERT NEW USER
    User userToInsert = new User(null, "Nelson", "Mandela", "nelson.mandela@gmail.com", "M", 33);
    entity = new HttpEntity<>(userToInsert, null);
    ResponseEntity<User> insertUserResponse = restTemplate
        .exchange("/api/v1/users", POST, entity, User.class, 1);
    assertThat(insertUserResponse.getStatusCode()).isEqualTo(OK);

    // GET - ALL USERS
    response = restTemplate.exchange("/api/v1/users", GET, null,
        personList);
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody().get(0)).isEqualToComparingFieldByField(userToUpdate);
    assertThat(response.getBody().get(1)).isEqualToIgnoringNullFields(userToInsert);

    // DELETE - USER BY ID=1
    ResponseEntity<String> deleteUserResponse = restTemplate
        .exchange("/api/v1/users/1", DELETE, null, String.class);
    assertThat(deleteUserResponse.getStatusCode()).isEqualTo(OK);
    response = restTemplate.exchange("/api/v1/users", GET, null,
        personList);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody().get(0)).isEqualToIgnoringNullFields(userToInsert);

  }
}
