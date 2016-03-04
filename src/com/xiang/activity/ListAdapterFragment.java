package com.xiang.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

/**
 * a fragment with a adapter inside
 * 
 * @author xiang.zc
 */
public abstract class ListAdapterFragment<T> extends Fragment implements
		OnItemClickListener {
	private final ListAdapter mListAdapter = new ListAdapter();

	protected abstract View getView(int position, View convertView,
			ViewGroup parent);

	public ListAdapter getListAdapter() {
		return (mListAdapter);
	}

	public class ListAdapter extends BaseAdapter {
		private List<T> mDataList = new ArrayList<T>();

		public boolean clearData() {
			Activity activity = getActivity();
			if (activity == null) {
				return (false);
			}
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (mDataList) {
						mDataList.clear();
						notifyDataSetChanged();
					}
				}
			});
			return (true);
		}

		public boolean resetData(final List<T> data) {
			Activity activity = getActivity();
			if (activity == null) {
				return (false);
			}
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (mDataList) {
						mDataList.clear();
						if (data != null) {
							mDataList.addAll(data);
						}
						notifyDataSetChanged();
					}
				}
			});
			return (true);
		}

		public boolean addData(final List<T> data) {
			Activity activity = getActivity();
			if (activity == null) {
				return (false);
			}
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (mDataList) {
						if (data != null) {
							mDataList.addAll(data);
						}
						notifyDataSetChanged();
					}
				}
			});
			return (true);
		}

		@Override
		public int getCount() {
			synchronized (mDataList) {
				return (mDataList.size());
			}
		}

		@Override
		public Object getItem(int position) {
			synchronized (mDataList) {
				if (position < mDataList.size()) {
					return (mDataList.get(position));
				}
				return (null);
			}
		}

		@Override
		public long getItemId(int position) {
			return (position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return (ListAdapterFragment.this.getView(position, convertView,
					parent));
		}
	}
}
