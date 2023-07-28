package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.comparators.CustomComparatorByPriority;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class User {

    private Account account;

    private Map<String, List<String>> folders;

    private Map<String, Collection<Mail>> mailsByFolder;

    private Queue<Rule> rules;

    private Set<String> recipients = new HashSet<>();
    private String sender = null;
    public User(String name, String email) {
        account = new Account(name, email);
        folders = new HashMap<>();
        folders.put("/inbox", new LinkedList<>());
        folders.put("/sent", new LinkedList<>());
        mailsByFolder = new HashMap<>();
        mailsByFolder.put("/inbox", new LinkedList<>());
        mailsByFolder.put("/sent", new LinkedList<>());
        rules = new PriorityQueue<>(new CustomComparatorByPriority());
    }

    private void checkEmails(Rule rule) {
        for (var currentMail : mailsByFolder.get("/inbox")) {
            if (!(rule.getSubjectIncludes() == null)) {
                boolean happened = false;
                for (var currentWord : rule.getSubjectIncludes()) {
                    if (!currentMail.subject().contains(currentWord)) {
                        happened = true;
                        break;
                    }
                }
                if (happened) {
                    continue;
                }
            } else if (!(rule.getSubjectAndBodyIncludes() == null)) {
                boolean happened = false;
                for (var currentWord : rule.getSubjectAndBodyIncludes()) {
                    String allInOne = currentMail.subject() + currentMail.body();
                    if (!allInOne.contains(currentWord)) {
                        happened = true;
                        break;
                    }
                }
                if (happened) {
                    continue;
                }
            } else if (!(rule.getRecipientsIncludes() == null)) {
                boolean happened = true;
                for (var currentWord : rule.getRecipientsIncludes()) {
                    if (currentMail.recipients().contains(currentWord)) {
                        happened = false;
                        break;
                    }
                }
                if (happened) {
                    continue;
                }
            } else if (!(rule.getFrom() == null)) {
                if (!currentMail.sender().emailAddress().equals(rule.getFrom())) {
                    continue;
                }
            }
            mailsByFolder.get("/inbox").remove(currentMail);
            if (rule.getFolderPath().size() > 0) {
                mailsByFolder.get(rule.getFolderPath().get(rule.getFolderPath().size() - 1)).add(currentMail);
            }
        }
    }

    private Mail splitData(String data, String content, boolean sent) {
        String[] words = data.split(System.lineSeparator());
        String senderToReturn = sender;
        String subject = null;
        String time = null;
        recipients = new HashSet<>();
        LocalDateTime dateTime = null;
        int subj = 0;
        int recived = 0;
        int recip = 0;
        int send = 0;
        for (var current : words ) {
            if (subj > 1 || recived > 1 || recip > 1 || send > 1) {
                throw new IllegalArgumentException("One of the mail topics is already defined");
            } else if (sent && current.startsWith("sender:")) {
                subj++;
                senderToReturn = current.substring(current.indexOf(":") + 1).strip();
            } else if (current.startsWith("subject:")) {
                recived++;
                subject = current.substring(current.indexOf(":") + 1).strip();
            } else if (current.startsWith("recipients:")) {
                recip++;
                String[] recipent = current.substring(current.indexOf(":") + 1).split(" ");
                for (var currentRecipient : recipent) {
                    recipients.add(currentRecipient.substring(0 , currentRecipient.length() - 1).strip());
                }
            } else if (current.startsWith("received:")) {
                send++;
                time = current.substring(current.indexOf(":") + 1).strip();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                dateTime = LocalDateTime.parse(time, formatter);
            }
        }
        return new Mail(new Account(senderToReturn, null), recipients, subject, content, dateTime);
    }
    public void addMail(String data, String content) {
        Mail newMail = splitData(data, content, true);
        mailsByFolder.get("/inbox").add(newMail);
        for (var currentRule : rules) {
            checkEmails(currentRule);
        }
    }

    public Set<String> addMailSent(String data, String content, String send) {
        sender = send;
        Mail newMail = splitData(data, content, false);
        mailsByFolder.get("/sent").add(newMail);
        for (var currentRule : rules) {
            checkEmails(currentRule);
        }
        sender = null;
        return recipients;
    }
    public void addRule(Rule rule) {
        if (rule.isLegitRule()) {
            checkEmails(rule);
            rules.add(rule);
        } else {
            throw new RuleAlreadyDefinedException("this rule is invalid");
        }
    }
    public Account getAccounts() {
        return account;
    }

    public Map<String, List<String>> getFolders() {
        return folders;
    }

    public Map<String, Collection<Mail>> getMailsByFolder() {
        return mailsByFolder;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}
