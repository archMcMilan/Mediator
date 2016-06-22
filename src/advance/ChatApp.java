package advance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 22.06.16.
 */
public class ChatApp {
    public static void main(String[] args) {
        Chat chat=new TextChat();

        User admin=new Admin(chat,"Ivan Ivanovich");
        User vania=new SimpleUser(chat,"Vania");
        User maksim=new SimpleUser(chat,"Maksim");
        User denis=new SimpleUser(chat,"Denis");
        maksim.setEnable(false);
        
        chat.setAdmin(admin);
        chat.addUser(vania);
        chat.addUser(maksim);
        chat.addUser(denis);
        vania.sendMessage("Where is my key?");
        admin.sendMessage("I don't know!");
    }
}

//client
abstract class User{
    Chat chat;
    String name;
    boolean isEnable=true;

    public User(Chat chat, String name) {
        this.chat = chat;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    void sendMessage(String message){
        chat.sendMessage(message,this);
    }
    abstract void getMessage(String message);
}

//Concrete Aggregate
class Admin extends User{
    public Admin(Chat chat, String name) {
        super(chat, name);
    }

    @Override
    public void getMessage(String message) {
        System.out.println("Admin "+getName() +" get message:"+message);
    }
}

class SimpleUser extends User{

    public SimpleUser(Chat chat, String name) {
        super(chat, name);
    }

    @Override
    public void getMessage(String message) {
        System.out.println("User "+getName() +" get message:"+message);
    }
}


//Mediator
interface Chat{
    void sendMessage(String message,User user);

    void setAdmin(User admin);

    void addUser(User vania);
}

//Concrete Mediator
class TextChat implements Chat {
    User admin;
    List<User> users = new ArrayList<>();

    public void setAdmin(User admin) {
        if(admin!=null && admin instanceof Admin){
            this.admin = admin;
        }else{
            throw new RuntimeException("Not enough rights");
        }

    }

    public void addUser(User user) {
        if(admin==null){
            throw new RuntimeException("Chat hasn't admin");
        }
        if(user instanceof SimpleUser){
            users.add(user);
        }else{
            throw new RuntimeException("Admin can't be in another chat");
        }
    }

    @Override
    public void sendMessage(String message, User user) {
        if(user instanceof Admin){
            for (User u : users) {
                u.getMessage(user.getName()+": "+message);
            }
        }
        if(user instanceof SimpleUser){
            for (User u : users) {
                if (u != user && u.isEnable()) {
                    u.getMessage(user.getName() + ": " + message);
                }
            }
            if(admin.isEnable()){
                admin.getMessage(user.getName() + ": " + message);
            }
        }
    }
}
