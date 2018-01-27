package info.androidhive.uplus;

/**
 * Holding fata
 */

public class TransationsList {
    private String amount;
    private String phone;
    private String userName;
    private String status;
    private String transactionDate;
    private String transactionColor;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTransactionDate(){
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionColor(){
        return transactionColor;
    }

    public void setTransactionColor(String transactionColor) {
        this.transactionColor = transactionColor;
    }
}



