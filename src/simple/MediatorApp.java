package simple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 22.06.16.
 */
public class MediatorApp {
    public static void main(String[] args) {
        simple.TextChat chat=new simple.TextChat();

        User admin=new Admin(chat);
        User user1=new SimpleUser(chat);
        User user2=new SimpleUser(chat);

        chat.setAdmin(admin);
        chat.addUser(user1);
        chat.addUser(user2);

        user1.sendMessage("Hello, I'm user1");
        admin.sendMessage("I'm admin");
    }
}

//client
interface User{
    void sendMessage(String message);
    void getMessage(String message);
}

//Concrete Aggregate
class Admin implements User{
    simple.Chat chat;

    public Admin(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void sendMessage(String message) {
        chat.sendMessage(message,this);
    }

    @Override
    public void getMessage(String message) {
        System.out.println("simple.Admin get message:"+message);
    }
}

class SimpleUser implements User{
    simple.Chat chat;

    public SimpleUser(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void sendMessage(String message) {
        chat.sendMessage(message,this);
    }

    @Override
    public void getMessage(String message) {
        System.out.println("simple.User get message:"+message);
    }
}


//Mediator
interface Chat{
    void sendMessage(String message,User user);
}

//Concrete Mediator
class TextChat implements Chat{
    simple.User admin;
    List<User> users=new ArrayList<>();

    public void setAdmin(User admin) {
        this.admin = admin;
    }
    public void addUser(User user){
        users.add(user);
    }

    @Override
    public void sendMessage(String message, User user) {
        for(User u:users){
            u.getMessage(message);
        }
        admin.getMessage(message);
    }
}