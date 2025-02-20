package bankmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Transaction {
    void deposit(double amount);
    void withdraw(double amount) throws InsufficientFundsException;
    void transfer(BankAccount targetAccount, double amount) throws InsufficientFundsException;
}

abstract class BankAccount implements Transaction {
    private final int accountNumber;
    private String accountHolder;
    protected double balance;

    public BankAccount(int accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public int getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }

    abstract String getAccountType();

    public void deposit(double amount){
        if(amount > 0){
            this.balance += amount;
            System.out.println("Deposited: "+amount);
            System.out.println("New Balance: "+balance);
        }else{
            System.out.println("Invalid Deposit Amount");
        }
    }
    public void withdraw(double amount) throws InsufficientFundsException{
        if(balance < amount){
            throw new InsufficientFundsException("Insufficient Funds! Available Balance: " + balance);
        }else if(amount > 0){
            this.balance -= amount;
            System.out.println("Withdrew: "+amount);
            System.out.println("New Balance: "+balance);
        }else{
            System.out.println("Invalid Withdrawal Amount");
        }
    }
    public void transfer(BankAccount targetAccount , double amount) throws InsufficientFundsException{
        if(targetAccount == null){
            System.out.println("Invalid Target Account");
            return;
        }
        this.withdraw(amount);
        targetAccount.deposit(amount);
        System.out.println("Transferred: "+amount+" | From account: "+accountNumber+" | To Account: "+targetAccount.getAccountNumber());
    }
}

class SavingAccount extends BankAccount{
    private double interestRate;

    public SavingAccount(int accountNumber , String accountHolder , double balance , double interestRate){
        super(accountNumber, accountHolder, balance);
        this.interestRate = interestRate;
    }
    public void addInterest(){
        double interest = balance * (interestRate / 100);
        deposit(interest);
        System.out.println("Interest Added: "+interest);
        System.out.println("New Balance: "+balance);
    }
    public String getAccountType(){
        return "Saving Account";
    }
}
class CheckingAccount extends BankAccount{
    private double overDraft;

    public CheckingAccount(int accountNumber , String accountHolder , double balance , double overDraft){
        super(accountNumber, accountHolder, balance);
        this.overDraft = overDraft;
    }
    public void withdraw(double amount) throws InsufficientFundsException{
        if(amount > balance + overDraft){
            throw new InsufficientFundsException("Exceeded OverDraft Limit! Available Balance + OverDraft: "+(balance+overDraft));
        }else if(amount > 0){
            balance -= amount;
            System.out.println("Withdrew: "+amount);
            System.out.println("New Balance: "+balance);
        }else{
            System.out.println("Invalid Withdrawal Amount");
        }
    }
    public String getAccountType(){
        return "Checking Account";
    }
}
class Customer{
    private int customerID;
    private String name;
    private String phone;
    private List<BankAccount> accounts;

    public Customer(int customerID , String name , String phone){
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }
    public int getCustomerId() {
        return customerID; }
    public String getName() {
        return name; }
    public String getPhone() {
        return phone; }
    public List<BankAccount> getAccounts() {
        return accounts; }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }
    public void displayAccount(){
        for (BankAccount account : accounts) {
            System.out.println(account.getAccountType()+" | Account Balance: "+ account.getBalance());
        }
    }
}

public class Main{
    public static void main(String[] args){
        Customer customer = new Customer(1001, "Muhammad Arslan", "123-456-7890");
        SavingAccount savingAccount = new SavingAccount(2001, "Muhammad Arslan", 5000.0, 3.5);
        CheckingAccount checkingAccount = new CheckingAccount(3001, "Muhammad Arslan", 2000.0, 1000.0);
        
        customer.addAccount(savingAccount);
        customer.addAccount(checkingAccount);

        Scanner sc = new Scanner(System.in);
        while (true) { 
            System.out.println("Welcome to Bank");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");
            System.out.println("4. View Account");
            System.out.println("5. Exit");
            System.out.print("Enter Your Choice: ");
            int userChoice = sc.nextInt();
            try {
                switch (userChoice) {
                    case 1:
                        System.out.print("Enter Deposit Amount: ");
                        savingAccount.deposit(sc.nextDouble());
                        break;
                    case 2:
                        System.out.print("Enter Withdrawal Amount: ");
                        savingAccount.withdraw(sc.nextDouble());
                        break;
                    case 3:
                        System.out.print("Enter Transfer Amount: ");
                        savingAccount.transfer(checkingAccount, sc.nextDouble());
                        break;
                    case 4:
                        customer.displayAccount();
                        break;
                    case 5:
                        System.out.println("Thank You For Visiting");
                        return;
                    default:
                        System.out.println("Invalid Choice, Try Again");
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Transaction Error: " + e.getMessage());
            }
        }
    }
}
