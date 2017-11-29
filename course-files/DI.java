// Dependency Injection

public class EmailService {

  private final ContactListService contactListService;

  public EmailService() {
    this.contactListService = new ContactListService();
  }

  sendEmail() {
    contactListService.getContacts().forEach(() -> contactListService::send)
  }

}


public class MailChimpEmailService {

  private final ContactListService contactListService;

  public MailChimpEmailService() {
    this.contactListService = new ContactListService();
  }

  sendEmail() {
    contactListService.getContacts().forEach(() -> contactListService::send)
  }

}

public class ContactListService {

  public ContactListService(){

  }

  public List<Contacts> getContacts() {
    // ... db
    return ImmutableList.copyOf(...);
  }

  void send(Contact contact) {

  }
}
