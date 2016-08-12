package com.billionav.navi.component.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaseBottomBar extends ViewGroup {

	private int visibleCount;
	private int maxTotalWidth;
	private int rowNum;
	private int verticalSpace;

	public BaseBottomBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public BaseBottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * measure children width and height
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		final int count = getChildCount();
		visibleCount = 0;
		int totalHeight = 0;
		int totalWidth = 0;
		int useHeight = 0;
		rowNum = 1;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}
			BaseBottomBar.LayoutParams lp = (BaseBottomBar.LayoutParams) child
					.getLayoutParams();
			if (lp.layoutDisable) {
				continue;
			}

			if (lp.comline || lp.rowline) {
				measureChildWithMargins(child, widthMeasureSpec, 0,
						heightMeasureSpec, 0);
			} else {
				visibleCount++;
				if (lp.exclusiveLine) {
					totalWidth = 0;
					rowNum++;
				}

				measureChildWithMargins(child, widthMeasureSpec, totalWidth,
						heightMeasureSpec, useHeight);

				totalWidth += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
				if (lp.isSquare) {
					totalHeight = Math.max(child.getMeasuredWidth()
							+ lp.topMargin + lp.bottomMargin, totalHeight);
				} else {
					totalHeight = Math.max(child.getMeasuredHeight()
							+ lp.topMargin + lp.bottomMargin, totalHeight);
				}
				if (lp.exclusiveLine) {
					useHeight += child.getMeasuredHeight() + lp.topMargin
							+ lp.bottomMargin;
				}
			}

		}

		maxTotalWidth = Math.max(maxTotalWidth, totalWidth);

		totalHeight += verticalSpace;

		setMeasuredDimension(resolveSize(maxTotalWidth, widthMeasureSpec),
				resolveSize(totalHeight * rowNum, heightMeasureSpec));

	}

	/**
	 * layout children
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		if (maxTotalWidth > r - l) {

		} else {
			int avalWidth = (r - l) / visibleCount;
			int avalHeight = (b - t) / rowNum;
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				BaseBottomBar.LayoutParams lp = (BaseBottomBar.LayoutParams) child
						.getLayoutParams();
				if (child.getVisibility() == GONE || lp.layoutDisable) {
					continue;
				}
				int width = child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
				if (width > avalWidth && !lp.exclusiveLine) {
					avalWidth = (r - l - width) / (visibleCount - 1);
					break;
				}
			}

			int left = 0;
			int top = 0;
			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				BaseBottomBar.LayoutParams lp = (BaseBottomBar.LayoutParams) child
						.getLayoutParams();
				if (child.getVisibility() == GONE || lp.layoutDisable) {
					continue;
				}

				int width = child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
				int height = 0;
				if (lp.isSquare) {
					height = child.getMeasuredWidth() + lp.bottomMargin
							+ lp.topMargin;

				} else {
					height = child.getMeasuredHeight() + lp.bottomMargin
							+ lp.topMargin;

				}
				int tempTop = top + (avalHeight - height) / 2;

				if (lp.comline) {
					child.layout(left - width / 2, top, left + width / 2, top
							+ avalHeight);
				} else if (lp.rowline) {
					child.layout(left, top - height / 2, r, top + height / 2);
				} else if (lp.exclusiveLine) {
					child.layout(left, tempTop, r, tempTop + height);
					left = 0;
					top += avalHeight;
				} else if (width > avalWidth) {
					child.layout(left, tempTop, left + width, tempTop + height);
					left += width;
				} else {
					int tempLeft = left + (avalWidth - width) / 2;
					child.layout(tempLeft, tempTop, tempLeft + width, tempTop
							+ height);

					left += avalWidth;
				}

			}

		}
	}

	public int getVerticalSpace() {
		return verticalSpace;
	}

	public void setVerticalSpace(int verticalSpace) {
		this.verticalSpace = verticalSpace;
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new BaseBottomBar.LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new BaseBottomBar.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new BaseBottomBar.LayoutParams(p);
	}

	// Override to allow type-checking of LayoutParams.
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof BaseBottomBar.LayoutParams;
	}

	public static class LayoutParams extends MarginLayoutParams {

		public boolean exclusiveLine = false;
		public boolean layoutDisable = false;
		public boolean canReduce = false;
		public boolean comline = false;
		public boolean rowline = false;
		public boolean isSquare = false;

		public LayoutParams(Context context, AttributeSet attr) {
			super(context, attr);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(int width, int height, boolean exclusiveLine) {
			super(width, height);
			this.exclusiveLine = exclusiveLine;
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		/**
		 * {@inheritDoc}
		 */
		public LayoutParams(ViewGroup.MarginLayoutParams source) {
			super(source);
		}

	}
}
