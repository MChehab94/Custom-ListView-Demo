package mchehab.com.customlistviewimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

public class ActivityAdd extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextDescription;
    private Spinner spinnerImage;

    private List<String> listImages;

    private Person person = new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        listImages = Arrays.asList(getResources().getStringArray(R.array.images));

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextDescription = findViewById(R.id.editTextDescription);

        spinnerImage = findViewById(R.id.spinnerImage);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(e -> {
            if(isValid()){
                Intent intent = new Intent();
                person.setFirstName(editTextFirstName.getText().toString());
                person.setLastName(editTextLastName.getText().toString());
                person.setDescription(editTextDescription.getText().toString());
                person.setImageName((String)spinnerImage.getSelectedItem());
                Bundle bundle = new Bundle();
                bundle.putParcelable("person", Parcels.wrap(person));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                showAlertDialog();
            }
        });

        checkExtras();
    }

    private void checkExtras(){
        if(hasExtras()){
            person = Parcels.unwrap(getIntent().getExtras().getParcelable("person"));
            editTextFirstName.setText(person.getFirstName());
            editTextLastName.setText(person.getLastName());
            editTextDescription.setText(person.getDescription());
            for(int i=0;i<listImages.size();i++){
                if(listImages.get(i).equals(person.getImageName())){
                    spinnerImage.setSelection(i);
                    break;
                }
            }
        }
    }

    private boolean hasExtras(){
        return getIntent().getExtras() != null;
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