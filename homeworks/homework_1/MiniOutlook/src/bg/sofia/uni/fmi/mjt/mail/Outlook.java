package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;

import java.util.*;

public class Outlook implements MailClient {

    public Map<String, User> getUsers() {
        return users;
    }

    private Map<String, User> users; //unique email!

    private LinkedList<String> pathFromString;
    private Queue<String> stringPathToQueue(String path) {
        pathFromString = new LinkedList<>();
        Queue<String> toReturn = pathFromString;
        if (!(path.startsWith("/inbox") || path.startsWith("/sent"))) {
            throw new InvalidPathException("path is invalid");
        }
        int sizeOfPath = path.length();
        int start = 0;
        for (int i = 0; i < sizeOfPath; i++) {
            if (i + 1 == sizeOfPath || path.charAt(i + 1) == '/') {
                toReturn.add(path.substring(start, i + 1));
                start = i + 1;
            }
        }
        return toReturn;
    }

    public Outlook() {
        users = new HashMap<>();
        pathFromString = new LinkedList<>();
    }

    @Override
    public Account addNewAccount(String accountName, String email) {
        if (accountName == null || email == null || accountName.isEmpty() || accountName.isBlank()
                || email.isEmpty() || email.isBlank()) {
            throw new IllegalArgumentException("String parameters (accountName or email) is null, empty or blank");
        } else if (users.containsKey(accountName)) {
            throw new AccountAlreadyExistsException("this account already exits");
        } else {
            for (User curr: users.values()) {
                if (curr.getAccounts().emailAddress().equals(email)) {
                    throw new AccountAlreadyExistsException("this email already exits");
                }
            }
        }
        users.put(accountName, new User(email, accountName));
        return users.get(accountName).getAccounts();
    }

    @Override
    public void createFolder(String accountName, String path) {
        if (accountName == null || path == null || accountName.isEmpty() || accountName.isBlank()
                || path.isEmpty() || path.isBlank()) {
            throw new IllegalArgumentException("String parameters (accountName or path) is null, empty or blank");
        } else if (!users.containsKey(accountName)) {
            throw new AccountNotFoundException("Account with such a name is not found");
        }
        Queue<String> pathQueue = stringPathToQueue(path);
        User currOne = users.get(accountName);
        String lastFolder = null;
        boolean sameAlreadyExists = true;
        while (!pathQueue.isEmpty()) {
            if (!currOne.getFolders().containsKey(pathQueue.peek())) {
                sameAlreadyExists = false;
                String newFolder = pathQueue.peek();
                pathQueue.remove();
                if (pathQueue.isEmpty() && lastFolder != null) {
                    currOne.getFolders().get(lastFolder).add(newFolder);
                    currOne.getMailsByFolder().put(newFolder, new LinkedList<>());
                    currOne.getFolders().put(newFolder, new LinkedList<>());
                    break;
                } else {
                    throw new InvalidPathException("there are missing folders");
                }
            }
            lastFolder = pathQueue.peek();
            pathQueue.remove();
        }
        if (sameAlreadyExists) {
            throw new FolderAlreadyExistsException("This folder already exist");
        }
    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        //check the folder
        //first stringPathToQueue
        //List!!!
        final int numberUp = 10;
        if (accountName == null || folderPath == null || ruleDefinition == null || accountName.isEmpty() ||
                accountName.isBlank() || folderPath.isEmpty() || folderPath.isBlank() || ruleDefinition.isEmpty()
                || ruleDefinition.isBlank() || priority < 1 || priority > numberUp ) {
            throw new IllegalArgumentException("String parameters or priority is invalid");
        } else if (!users.containsKey(accountName)) {
            throw new AccountNotFoundException("there is no such an account");
        }
        Queue<String> currentFolderPath = stringPathToQueue(folderPath);
        int size = currentFolderPath.size();
        for (int i = 0; i < size; i++) {
            String folder = currentFolderPath.peek();
            if (!users.get(accountName).getFolders().containsKey(folder)) {
                throw new FolderNotFoundException("no such a folder");
            }
            currentFolderPath.remove();
            if (!currentFolderPath.isEmpty() && !users.get(accountName).getFolders()
                    .get(folder).contains(currentFolderPath.peek())) {
                throw new FolderNotFoundException("no such a folder");
            }
        }
        Rule newRule = new Rule(pathFromString, ruleDefinition, priority);
        users.get(accountName).addRule(newRule);
    }


    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        checkParameters(accountName, mailMetadata, mailContent);
        users.get(accountName).addMail(mailMetadata, mailContent);
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        if (account == null || folderPath == null || account.isEmpty() || account.isBlank()
                || folderPath.isEmpty() || folderPath.isBlank()) {
            throw new IllegalArgumentException("String parameters  is null, empty or blank");
        } else if (!users.containsKey(account)) {
            throw new AccountNotFoundException("Account with such a name is not found");
        }
        stringPathToQueue(folderPath);
        if (!users.get(account).getMailsByFolder().containsKey(pathFromString.get(pathFromString.size() - 1))) {
            throw new FolderNotFoundException("no such a folder");
        }
        return Collections.unmodifiableCollection(users.get(account).getMailsByFolder().get(pathFromString
                .get(pathFromString.size() - 1)));
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        checkParameters(accountName, mailMetadata, mailContent);
        users.get(accountName).setSender(accountName);
        Set<String> other = new HashSet<>(users.get(accountName).addMailSent(mailMetadata, mailContent, accountName));
        for (var current: other) {
            for (var currentUser : users.values()) {
                if (currentUser.getAccounts().emailAddress().equals(current)) {
                    currentUser.addMail(mailMetadata, mailContent);
                    break;
                }
            }
        }

    }

    private void checkParameters(String accountName, String mailMetadata, String mailContent) {
        if (accountName == null || mailContent == null || mailMetadata == null || accountName.isEmpty()
                || accountName.isBlank()
                || mailContent.isEmpty() || mailContent.isBlank() || mailMetadata.isEmpty() || mailMetadata.isBlank()) {
            throw new IllegalArgumentException("String parameters (accountName or path) is null, empty or blank");
        }
        else if (!users.containsKey(accountName)) {
            throw new AccountNotFoundException("Account with such a name is not found");
        }
    }
}
