package rocks.zipcode.atm.bank;

import rocks.zipcode.atm.ActionResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ZipCodeWilmington
 */
public class Bank {

    private Map<Integer, Account> accounts = new HashMap<>();
    private static int numberOfAccounts = 1000;

    public Bank() {
        addAccount("Example 1", "example1@gmail.com", 100, false);
        addAccount("Example 2", "example2@aol.com", 200, true);
    }

    public void addAccount(String name, String email, float balance, boolean premium){
        numberOfAccounts ++;
        if(premium) {
            accounts.put(numberOfAccounts, new BasicAccount(new AccountData(
                    numberOfAccounts, name, email, balance, false)));
        } else {
            accounts.put(numberOfAccounts, new PremiumAccount(new AccountData(
                    numberOfAccounts, name, email, balance, true)));
        }
    }

    public ActionResult<AccountData> getAccountById(int id) {
        Account account = accounts.get(id);

        if (account != null) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("No account with id: " + id + "\nTry account 1000 or 2000");
        }
    }

    public ActionResult<AccountData> deposit(AccountData accountData, float amount) {
        Account account = accounts.get(accountData.getId());
        account.deposit(amount);

        return ActionResult.success(account.getAccountData());
    }

    public ActionResult<AccountData> withdraw(AccountData accountData, float amount) {
        Account account = accounts.get(accountData.getId());
        boolean ok = account.withdraw(amount);

        if (ok) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("Withdraw failed: " + amount + ". Account has: " + account.getBalance());
        }
    }

    public Set<Integer> getAccountIds() {
        return accounts.keySet();
    }

    public int getNumberOfAccounts() {
        return numberOfAccounts;
    }


}
