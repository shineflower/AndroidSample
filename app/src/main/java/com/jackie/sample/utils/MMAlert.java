package com.jackie.sample.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.tencent.mm.sdk.platformtools.Util;

import java.util.ArrayList;
import java.util.List;

public final class MMAlert {

	public interface OnAlertSelectedListener {
		void onSelected(int whichButton);
	}

	private MMAlert() {
	}

	public static AlertDialog showAlert(Context context, int titleId, int msgId,
			DialogInterface.OnClickListener okClickListener, final DialogInterface.OnClickListener cancelClickListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing()) {
			return null;
		}

		Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_dialog_alert);
		builder.setTitle(titleId);
		builder.setMessage(msgId);
		builder.setPositiveButton(R.string.app_ok, okClickListener);
		builder.setNegativeButton(R.string.app_cancel, cancelClickListener);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();

		return alertDialog;
	}


	public static Dialog showAlert(Context context, String title, String[] items,
			String exit, OnAlertSelectedListener onAlertSelectedListener) {
		return showAlert(context, title, items, exit, onAlertSelectedListener, null);
	}

	/**
	 * @param context  Context.
	 * @param title The title of this AlertDialog can be null .
	 * @param items button name list.
	 * @param OnAlertSelectedListener methods call Id:Button + cancel_Button.
	 * @param exit Name can be null.It will be Red Color
	 * @return A AlertDialog
	 */
	public static Dialog showAlert(Context context, final String title, String[] items, String exit,
			final OnAlertSelectedListener OnAlertSelectedListener, OnCancelListener cancelListener) {
		String cancel = context.getString(R.string.app_cancel);
		final Dialog dialog = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.alert_dialog_menu_layout, null);
		int minimumWidth = 10000;
        linearLayout.setMinimumWidth(minimumWidth);
		final ListView listView = (ListView) linearLayout.findViewById(R.id.content_list);
		AlertAdapter adapter = new AlertAdapter(context, title, items, exit, cancel);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);

		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!TextUtils.isEmpty(title) && position - 1 >= 0) {
					OnAlertSelectedListener.onSelected(position - 1);
					dialog.dismiss();
					listView.requestFocus();
				} else {
					OnAlertSelectedListener.onSelected(position);
					dialog.dismiss();
					listView.requestFocus();
				}
			}
		});

		// set a large value put it in bottom
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		int bottom = -1000;
		lp.y = bottom;
		lp.gravity = Gravity.BOTTOM;
		dialog.onWindowAttributesChanged(lp);
		dialog.setCanceledOnTouchOutside(true);
		if (cancelListener != null) {
			dialog.setOnCancelListener(cancelListener);
		}

		dialog.setContentView(linearLayout);
		dialog.show();

		return dialog;
	}
}

class AlertAdapter extends BaseAdapter {
	public static final int TYPE_BUTTON = 0;
	public static final int TYPE_TITLE = 1;
	public static final int TYPE_EXIT = 2;
	public static final int TYPE_CANCEL = 3;

	private List<String> items;
	private int[] types;
	private boolean isTitle = false;
	private Context context;

	public AlertAdapter(Context context, String title, String[] items, String exit, String cancel) {
		if (items == null || items.length == 0) {
			this.items = new ArrayList<>();
		} else {
			this.items = Util.stringsToList(items);
		}
		this.types = new int[this.items.size() + 3];
		this.context = context;
		if (!TextUtils.isEmpty(title)) {
			types[0] = TYPE_TITLE;
			this.isTitle = true;
			this.items.add(0, title);
		}

		if (!TextUtils.isEmpty(exit)) {
			// this.isExit = true;
			types[this.items.size()] = TYPE_EXIT;
			this.items.add(exit);
		}

		if (!TextUtils.isEmpty(cancel)) {
			// this.isSpecial = true;
			types[this.items.size()] = TYPE_CANCEL;
			this.items.add(cancel);
		}
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		if (position == 0 && isTitle) {
			return false;
		} else {
			return super.isEnabled(position);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String textString = (String) getItem(position);
		ViewHolder holder;
		int type = types[position];
		if (convertView == null || ((ViewHolder) convertView.getTag()).type != type) {
			holder = new ViewHolder();
			if (type == TYPE_CANCEL) {
				convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_cancel, null);
			} else if (type == TYPE_BUTTON) {
				convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_button, null);
			} else if (type == TYPE_TITLE) {
				convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_title, null);
			} else if (type == TYPE_EXIT) {
				convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_special, null);
			}

			holder.text = (TextView) convertView.findViewById(R.id.popup_text);
			holder.type = type;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(textString);
		return convertView;
	}

	static class ViewHolder {
		TextView text;
		int type;
	}
}
