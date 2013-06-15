package com.blockingqueuetests;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements UIUpdater{

    private Button mButton1, mButton2, mButton3;

    private TextView mQueueTextView;
    private TextView mCurrentProductTextView;

    private Coordinator mCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton1 = (Button)findViewById(R.id.producer1);
        mButton2 = (Button)findViewById(R.id.producer2);
        mButton3 = (Button)findViewById(R.id.producer3);

        mQueueTextView = (TextView)findViewById(R.id.currentqueueValues);
        mCurrentProductTextView = (TextView)findViewById(R.id.elementProcessedValue);

        View.OnClickListener clickListener = new BtnClickListener();

        mButton1.setOnClickListener(clickListener);
        mButton2.setOnClickListener(clickListener);
        mButton3.setOnClickListener(clickListener);

        mCoordinator = new CoordinatorImpl(this);
    }

    @Override
    public void onQueueChanged(String queueAsText) {
        mQueueTextView.setText(queueAsText);
    }

    @Override
    public void onProductInProcessing(Product p) {
        mCurrentProductTextView.setText(p.toString());
    }

    @Override
    public void onProductProcessed(Product p) {
        mCurrentProductTextView.setText("---");
    }

    private class BtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(view instanceof Button){
                String name = ((Button)view).getText().toString();
                Product p = createProduct(name);
                mCoordinator.offer(p);
            }else{
                throw new IllegalArgumentException();
            }
        }
    }

    private Product createProduct(String name){
        Product p = new ProductImpl(name);
        return p;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
