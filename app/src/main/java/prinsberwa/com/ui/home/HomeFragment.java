package prinsberwa.com.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import prinsberwa.com.R;
import prinsberwa.com.prince.IGRefreshLayout;

public class HomeFragment extends Fragment {

    private IGRefreshLayout swipe;
    private RecyclerView recyclerView;
    private TextView helloWorldText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        swipe = rootView.findViewById(R.id.swipe);
        recyclerView = rootView.findViewById(R.id.rv);
        helloWorldText = rootView.findViewById(R.id.hello_world_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipe.setRefreshListener(() -> {
            new Handler().postDelayed(() -> swipe.setRefreshing(false), 1500);
        });

        return rootView;
    }
}