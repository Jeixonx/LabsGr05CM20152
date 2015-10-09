package co.edu.udea.cmovil.gr5.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class StatusActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if this activity was created before
        if (savedInstanceState == null) {
            // Create a fragment
            StatusFragment fragment = new StatusFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }

    // Called when an options item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemServiceStart:
                startService(new Intent(this, RefreshService.class));
                break;
       //     case R.id.itemPrefs:
           //     startActivity(new Intent(this, PrefsActivity.class));
         //       break;
            default:
                return false;
        }
        return true;
    }

}