package mchehab.com.customlistviewimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ActivityAdd extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDescription = findViewById(R.id.editTextDescription);

        Spinner spinnerImage = findViewById(R.id.spinnerImage);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(e -> {
            if(isValid()){
                Intent intent = new Intent();
                intent.putExtra("firstName", editTextFirstName.getText().toString());
                intent.putExtra("lastName", editTextLastName.getText().toString());
                intent.putExtra("description", editTextDescription.getText().toString());
                intent.putExtra("imageName", (String)spinnerImage.getSelectedItem());
                setResult(RESULT_OK, intent);
                finish();
            }else{
                showAlertDialog();
            }
        });
    }

    private boolean isValid(){
        if(isEditTextEmpty(editTextFirstName))
            return false;
        if(isEditTextEmpty(editTextLastName))
            return false;
        if(isEditTextEmpty(editTextDescription))
            return false;
        return true;
    }

    private boolean isEditTextEmpty(EditText editText){
        return editText.getText().toString().length() == 0;
    }

    private void showAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Make sure to fill all of the fields")
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .create()
                .show();
    }

}
