package com.fundit.helper;



import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;

public class CustomDialog extends ProgressDialog {
	Context context;
	String comment;
	ImageView la;
	TextView txt_loading;

	public CustomDialog(Context context){
		super(context);
		this.context = context;
		this.comment = "Please wait...";
	}

	public CustomDialog(Context context, String comment) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.comment = comment.isEmpty() ? "Please wait..." : comment;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_custom_dialog);
		getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

		this.setCancelable(false);
		txt_loading=(TextView) findViewById(R.id.txt_loading);

		txt_loading.setText(comment);

	}

	@Override
	public void show() {
		super.show();

	}

	@Override
	public void dismiss() {
		super.dismiss();
	}
}
