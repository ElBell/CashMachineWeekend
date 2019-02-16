package rocks.zipcode.atm.bank;

/**
 * @author ZipCodeWilmington
 */
public final class AccountData {

    private final int id;
    private final String name;
    private final String email;
    private final float balance;
    private final boolean premium;

    public AccountData(int id, String name, String email, float balance, boolean premium) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.premium = premium;
    }

    public int getId() {return id;}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public float getBalance() {
        return balance;
    }

    public boolean isPremium() {return premium; }


    @Override
    public String toString() {
        return "Account id: " + id + '\n' +
                "Name: " + name + '\n' +
                "Email: " + email + '\n' +
                "Balance: " + balance;
    }
}
