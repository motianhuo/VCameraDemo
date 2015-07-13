package com.example.wechat01.widght.swipe.interfaces;

import java.util.List;

import com.example.wechat01.widght.swipe.SwipeLayout;
import com.example.wechat01.widght.swipe.implments.SwipeItemMangerImpl;

public interface SwipeItemMangerInterface {

	public void openItem(int position);

	public void closeItem(int position);

	public void closeAllExcept(SwipeLayout layout);

	public List<Integer> getOpenItems();

	public List<SwipeLayout> getOpenLayouts();

	public void removeShownLayouts(SwipeLayout layout);

	public boolean isOpen(int position);

	public SwipeItemMangerImpl.Mode getMode();

	public void setMode(SwipeItemMangerImpl.Mode mode);
}
