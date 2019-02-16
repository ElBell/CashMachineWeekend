import org.junit.*;
import rocks.zipcode.atm.bank.Account;
import rocks.zipcode.atm.bank.AccountData;

public class TestAccountData {

    @Test
    public void testCreateAccountData() {
        //Given
        int id = 1;
        String name = "nameThis";
        String email = "emailThis";
        float balance = 60;
        boolean premium = false;

        //When
        AccountData newAccountData = new AccountData(id, name, email, balance, premium);

        //Then
        int actualId = newAccountData.getId();
        String actualName = newAccountData.getName();
        String actualEmail = newAccountData.getEmail();
        float actualBalance = newAccountData.getBalance();

        Assert.assertEquals(id, actualId);
        Assert.assertEquals(name, actualName);
        Assert.assertEquals(email, actualEmail);
        Assert.assertEquals(balance, actualBalance, 0.01f);
        Assert.assertFalse(premium);
    }

    @Test
    public void testCreateAccountData2() {
        //Given
        int id = 100;
        String name = "otherName";
        String email = "otherEmail";
        float balance = 60000;
        boolean premium = true;

        //When
        AccountData newAccountData = new AccountData(id, name, email, balance, premium);

        //Then
        int actualId = newAccountData.getId();
        String actualName = newAccountData.getName();
        String actualEmail = newAccountData.getEmail();
        float actualBalance = newAccountData.getBalance();

        Assert.assertEquals(id, actualId);
        Assert.assertEquals(name, actualName);
        Assert.assertEquals(email, actualEmail);
        Assert.assertEquals(balance, actualBalance, 0.01f);
        Assert.assertTrue(premium);
    }

}
