package in.divspace.stacksearch;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    SearchView searchView;
    ProgressBar progressBar;
    FloatingActionButton forwardButton,backButton;

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()) {
            webView.goBack();
        }else{
            showAlert();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        searchView = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);
        forwardButton = findViewById(R.id.forwardButton);
        backButton = findViewById(R.id.backButton);

        //SearchView will be selected (The Keyboard will Open) on Activity START
        searchView.setIconified(false);

        //Previous Page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack())
                    webView.goBack();
                else
                    onBackPressed();
            }
        });

        //Next Page
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchView.hasFocus())
                    searchView.setQuery(searchView.getQuery(),true);//submit current query
                else if(webView.canGoForward())
                    webView.goForward();
            }
        });

        // Whole SearchBar Can be Clicked
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        //When The SearchQuery Will Be Submitted
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    //Load The SearchResults for the query
    private void loadSearch(String query){
        searchView.clearFocus(); // Clears the searchView Focus (Close Keyboard)
        String google = "https://www.google.com/search?q=";
        String q = query + " " + "inurl:stackoverflow.com";

        try {
            q = URLEncoder.encode(q,"UTF-8"); /*Encode query for special symbol use (+,&,...) */
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = google + q;

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                setProgress(newProgress * 100); //Make the bar disappear after URL is loaded
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                // Return the app name after finish loading
                if(newProgress == 100) {
                    setTitle(R.string.app_name);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private void showAlert(){
        Context context = this;

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder .setMessage("Are you sure you want to Exit?")
                /*Positive Button is Yes Button*/
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                /*Negative Button is Cancel Button*/
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
