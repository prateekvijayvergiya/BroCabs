package madprateek.brocabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void isSelected(View v){
        Boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.driverBtn:
                if (checked){

                    Intent driverIntent = new Intent(MainActivity.this,DriverActivity.class);
                    startActivity(driverIntent);
                }
                break;
            case R.id.customerBtn:
                if (checked){

                    Intent customerIntent = new Intent(MainActivity.this,CustomerActivity.class);
                    startActivity(customerIntent);
                }
                break;

        }
    }
}
