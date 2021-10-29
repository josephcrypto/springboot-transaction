package com.example.springboottransaction.dao;

import com.example.springboottransaction.entity.BankAccount;
import com.example.springboottransaction.exception.BankTransactionException;
import com.example.springboottransaction.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class BankAccountDAO {

    @Autowired
    private EntityManager entityManager;

    public BankAccountDAO(){
    }

    public BankAccount findById(Long id) {
        return this.entityManager.find(BankAccount.class, id);
    }

    public List<BankAccountInfo> listBankAccountInfo(){
        String sql = "Select new " + BankAccountInfo.class.getName()
                                   + "(e.id, e.fullName, e.balance)"
                                   + " from " + BankAccount.class.getName() + " e ";
        Query query = entityManager.createQuery(sql, BankAccountInfo.class);
        return query.getResultList();
    }

    //Mandatory : Transaction must be created before
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount(Long id, double amount) throws BankTransactionException {
        BankAccount account = this.findById(id);
        if (account == null) {
            throw new BankTransactionException("Account not found " + id);
        }
        double newBalance = account.getBalance() + amount;
        if (account.getBalance() + amount < 0) {
            throw  new BankTransactionException("The Money in the account '" + id + "' is not enough(" + account.getBalance() + ")");
        }
        account.setBalance(newBalance);
    }

    //Do not catch BankTransactionException in this method
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankTransactionException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, double amount) throws BankTransactionException {
        addAmount(toAccountId, amount);
        addAmount(fromAccountId, -amount);
    }
}
