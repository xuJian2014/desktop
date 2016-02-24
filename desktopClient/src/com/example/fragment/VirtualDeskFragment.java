package com.example.fragment;

import com.example.desktop.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class VirtualDeskFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View logonView = inflater.inflate(R.layout.fragment_virtual_service,
				null);

		return logonView;
	}

}
