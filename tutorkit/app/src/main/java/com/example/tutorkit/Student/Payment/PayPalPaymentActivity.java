package com.example.tutorkit.Student.Payment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorkit.R;
//import com.paypal.checkout.approve.Approval;
//import com.paypal.checkout.approve.OnApprove;
//import com.paypal.checkout.createorder.CreateOrder;
//import com.paypal.checkout.createorder.CreateOrderActions;
//import com.paypal.checkout.createorder.CurrencyCode;
//import com.paypal.checkout.createorder.OrderIntent;
//import com.paypal.checkout.createorder.UserAction;
//import com.paypal.checkout.order.Amount;
//import com.paypal.checkout.order.AppContext;
//import com.paypal.checkout.order.CaptureOrderResult;
//import com.paypal.checkout.order.OnCaptureComplete;
//import com.paypal.checkout.order.OrderRequest;
//import com.paypal.checkout.order.PurchaseUnit;
//import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class PayPalPaymentActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
//    PaymentButtonContainer paymentButtonContainer;
    TextView txt_total;
    int total;

//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Sandbox for testing
//            .clientId("YOUR_CLIENT_ID_HERE"); // Replace with your own client ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);

        total = getIntent().getIntExtra("total", 0);
        txt_total = findViewById(R.id.txt_total);
        txt_total.setText(String.valueOf(total));

//        paymentButtonContainer = findViewById(R.id.payment_button_container);
//
//        paymentButtonContainer.setup(
//                new CreateOrder() {
//                    @Override
//                    public void create(@NotNull CreateOrderActions createOrderActions) {
//                        Log.d(TAG, "create: ");
//                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
//                        purchaseUnits.add(
//                                new PurchaseUnit.Builder()
//                                        .amount(
//                                                new Amount.Builder()
//                                                        .currencyCode(CurrencyCode.USD)
//                                                        .value("10.00")
//                                                        .build()
//                                        )
//                                        .build()
//                        );
//                        OrderRequest order = new OrderRequest(
//                                OrderIntent.CAPTURE,
//                                new AppContext.Builder()
//                                        .userAction(UserAction.PAY_NOW)
//                                        .build(),
//                                purchaseUnits
//                        );
//                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
//                    }
//                },
//                new OnApprove() {
//                    @Override
//                    public void onApprove(@NotNull Approval approval) {
//                        approval.getOrderActions().capture(new OnCaptureComplete() {
//                            @Override
//                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
//                                Log.d(TAG, String.format("CaptureOrderResult: %s", result));
//                                Toast.makeText(PayPalPaymentActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}