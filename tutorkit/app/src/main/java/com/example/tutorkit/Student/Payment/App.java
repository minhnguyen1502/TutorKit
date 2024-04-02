package com.example.tutorkit.Student.Payment;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AfE_-4IywXGDLw05aAo0rYjZIU5XFlnsk17jKOiWfrS_H1O_yWAR40h3SEVxLZMLPELUknZlJ33lZxo6",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.example.tutorkit//paypalpay"
        ));
    }
}
