package com.jacelyn.ezyfood;

public class WalletAmount {
    final int wallet;

    private WalletAmount(int wallet){
        this.wallet = wallet;
    }

    static WalletAmount getInstance(int wallet){
        if(instance == null){
            instance = new WalletAmount(wallet);
        }
        return instance;
    }

    private static WalletAmount instance;
}
