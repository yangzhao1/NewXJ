package com.zq.xinjiang.approval.progressdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zq.xinjiang.R;

/**
 * The <code>CustomProgressDialog</code> class is used to initialize a custom
 * progress dialog with expected style.
 * 
 * @author index_cqq
 * @version 1.0, 8 September 2015
 */
public class CustomProgressDialog extends Dialog {

	private Context mContext = null;
	private static CustomProgressDialog mCustomProgressDialog = null;

	/**
	 * Creates an instance of <code>CustomProgressDialog</code>.
	 * 
	 * @param context
	 *            context allows access to resources and types of features to
	 *            the application.
	 */
	public CustomProgressDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * Creates an instance of <code>CustomProgressDialog</code>.
	 * 
	 * @param context
	 *            context allows access to resources and types of features to
	 *            the application.
	 * @param theme
	 *            theme.
	 */
	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * Creates a dialog.
	 * 
	 * @param context
	 *            context allows access to resources and types of features to
	 *            the application.
	 * @return the instance of CustomProgressDialog.
	 */
	public static CustomProgressDialog createDialog(Context context) {
		mCustomProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		mCustomProgressDialog.setContentView(R.layout.progressdialog_style);
		mCustomProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return mCustomProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (mCustomProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) mCustomProgressDialog
				.findViewById(R.id.image_loading_view);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * Sets title.
	 * 
	 * @param strTitle
	 *            title.
	 * @return the instance of CustomProgressDialog.
	 */
	public CustomProgressDialog setTitile(String strTitle) {
		return mCustomProgressDialog;
	}

	/**
	 * Sets message.
	 * 
	 * @param strMessage
	 *            message.
	 * @return the instance of CustomProgressDialog.
	 */
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) mCustomProgressDialog
				.findViewById(R.id.text_loading_msg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return mCustomProgressDialog;
	}
	
}