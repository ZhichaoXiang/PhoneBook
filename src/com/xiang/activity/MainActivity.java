package com.xiang.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.xiang.phonebook.R;

/**
 * main activity
 * 
 * @author xiang.zc
 */
public class MainActivity extends Activity {
	private final int MENU_CALL = 0;
	private final int MENU_CONTACT = 1;
	private final int MENU_MESSAGE = 2;
	private final CallFragment callFragment = new CallFragment();
	private final ContactFragment contactFragment = new ContactFragment();
	private final MessageFragment messageFragment = new MessageFragment();
	private ListView mListView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mListView = (ListView) findViewById(R.id.main_list);
		mListView.setFastScrollAlwaysVisible(true);
		initMainMenu((RadioGroup) findViewById(R.id.main_menu));
	}

	private void initMainMenu(RadioGroup mainMenu) {
		/**
		 * init ids
		 */
		int mainMenuChildCount = mainMenu.getChildCount();
		int i = 0;
		while (i < mainMenuChildCount) {
			mainMenu.getChildAt(i).setId(i);
			++i;
		}
		/**
		 * listener
		 */
		mainMenu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				final FragmentTransaction fragmentTransaction = getFragmentManager()
						.beginTransaction();
				ListAdapterFragment<?> fragment = null;
				switch (checkedId) {
				case MENU_CALL: {
					fragmentTransaction.show(callFragment)
							.hide(contactFragment).hide(messageFragment);
					fragment = callFragment;
				}
					break;
				case MENU_CONTACT: {
					fragmentTransaction.hide(callFragment)
							.show(contactFragment).hide(messageFragment);
					fragment = contactFragment;
				}
					break;
				case MENU_MESSAGE: {
					fragmentTransaction.hide(callFragment)
							.hide(contactFragment).show(messageFragment);
					fragment = messageFragment;
				}
					break;
				}
				if (fragment != null) {
					mListView.setAdapter(fragment.getListAdapter());
					mListView.setOnItemClickListener(fragment);
				}
				if ((group.getChildAt(checkedId).getTag() == null)
						&& (fragment != null)) {
					group.getChildAt(checkedId).setTag(fragment);
					fragmentTransaction.add(R.id.main_container, fragment);
				}
				fragmentTransaction.commit();
			}
		});
		/**
		 * check call
		 */
		mainMenu.check(MENU_CALL);
	}

}
