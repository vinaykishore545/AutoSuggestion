package in.www.myapplication3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;

public class Sreach extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener {
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PlacesAutoCompleteAdapter.PlaceAutocomplete> mResultList = new ArrayList<>();
    LinearLayoutManager llmV, llmH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        mResultList.clear();
        Toast.makeText(getApplicationContext(),String.valueOf(mResultList.size()),Toast.LENGTH_SHORT).show();
        Places.initialize(this, getResources().getString(R.string.place));
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        ((EditText) findViewById(R.id.plcae)).addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this,mResultList);
        layoutManager = new LinearLayoutManager(this);
        llmV = new LinearLayoutManager(this);
        llmV.setOrientation(LinearLayoutManager.VERTICAL);
        mAutoCompleteAdapter.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
        recyclerView.setLayoutManager(llmV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);


    }
    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(s.length() > 0) {
            }


        }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(this, place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();

    }

}
