package hult.netlab.pku.apmpowermanager;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMode.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAbout extends Fragment {


    public FragmentAbout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_about,container,false);
        WebView webView = new WebView(container.getContext());
        webView = (WebView)rootview.findViewById(R.id.webview);
        webView.loadUrl("http://hult1989.github.io/eos-apm/");
        return rootview;
    }
}
