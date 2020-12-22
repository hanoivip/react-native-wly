package org.cocos2dx.lib;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Typeface;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

public class Cocos2dxBitmap {
	private static final int ALIGNCENTER = 51;
	private static final int ALIGNLEFT = 49;
	private static final int ALIGNRIGHT = 50;
	private static Context context;

	private static class Component {
		private final Vector<String> mFontStyle;
		private final int mPosition;
		private final String mTag;
		private final String mText;

		public Component(String str, String str2, int i, Vector<String> vector) {
			this.mFontStyle = new Vector();
			this.mText = str;
			this.mTag = str2;
			this.mPosition = i;
			if (vector.size() > 0) {
				this.mFontStyle.addAll(vector);
			}
		}
	}

	private static class MyStringLine {
		public String content;
		public LinkedList<String> emojiList;

		public MyStringLine() {
			this.emojiList = new LinkedList();
		}

		public MyStringLine(String str, LinkedList<String> linkedList) {
			this.emojiList = new LinkedList();
			this.content = str;
			this.emojiList = linkedList;
		}

		public int getTw(Paint paint) {
			String str;
			String str2 = new String(this.content);
			if (this.emojiList.size() != 0) {
				System.out.println("getTw:" + str2);
				str = str2;
				for (int i = 0; i < this.emojiList.size(); i++) {
					str = str
							.replaceAll((String) this.emojiList.get(i), "    ");
				}
				System.out.println("getTw 1:" + str);
			} else {
				str = str2;
			}
			return (int) Math.ceil(paint.measureText(str));
		}
	}

	private static class TextInfo {
		public int h;
		public int lineIndex;
		public final String mText;
		public MyStringLine myStrLine;
		public Paint paint;
		public int w;
		public int x;

		public TextInfo(int i, String str, int i2, int i3, int i4, Paint paint) {
			this.lineIndex = i;
			this.mText = str;
			this.w = i2;
			this.h = i3;
			this.x = i4;
			this.paint = paint;
		}

		public TextInfo(int i, MyStringLine myStringLine, int i2, int i3,
				int i4, Paint paint) {
			this.lineIndex = i;
			this.mText = "";
			this.w = i2;
			this.h = i3;
			this.x = i4;
			this.paint = paint;
			this.myStrLine = myStringLine;
		}
	}

	private static class TextProperty {
		int heightPerLine;
		String[] lines;
		int maxWidth;
		int totalHeight;

		TextProperty(int i, int i2, String[] strArr) {
			this.maxWidth = i;
			this.heightPerLine = i2;
			this.totalHeight = strArr.length * i2;
			this.lines = strArr;
		}
	}

	private static TextProperty computeTextProperty(String str, Paint paint,
			int i, int i2) {
		int i3;
		int length;
		int i4;
		int i5;
		FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
		int ceil = (int) Math
				.ceil((double) (fontMetricsInt.bottom - fontMetricsInt.top));
		String[] splitString = splitString(str, i2, i, paint);
		if (i != 0) {
			i3 = i;
		} else {
			length = splitString.length;
			i4 = 0;
			i5 = 0;
			while (i4 < length) {
				String str2 = splitString[i4];
				i3 = (int) Math.ceil((double) paint.measureText(str2, 0,
						str2.length()));
				if (i3 <= i5) {
					i3 = i5;
				}
				i4++;
				i5 = i3;
			}
			i3 = i5;
		}
		if (i3 == 0) {
			for (String str3 : splitString) {
				i5 = (int) Math.ceil((double) paint.measureText(" ", 0,
						" ".length()));
				if (i5 > i3) {
					i3 = i5;
				}
			}
		}
		return new TextProperty(i3, ceil, splitString);
	}

	private static int computeX(Paint paint, String str, int i, int i2) {
		switch (i2) {
		case ALIGNRIGHT /* 50 */:
			return i;
		case ALIGNCENTER /* 51 */:
			return i / 2;
		default:
			return 0;
		}
	}

	public static void createTextBitmap(String str, String str2, int i, int i2,
			int i3, int i4) {
		String refactorString;
		int i5;
		int i6;
		if (!str.contains("begin@")) {
			if (!str.contains("@end")) {
				refactorString = refactorString(str);
				Paint newPaint = newPaint(str2, i, i2);
				TextProperty computeTextProperty = computeTextProperty(
						refactorString, newPaint, i3, i4);
				Bitmap createBitmap = Bitmap.createBitmap(
						computeTextProperty.maxWidth,
						i4 == 0 ? computeTextProperty.totalHeight : i4,
						Config.ARGB_8888);
				Canvas canvas = new Canvas(createBitmap);
				FontMetricsInt fontMetricsInt = newPaint.getFontMetricsInt();
				i5 = i4 == 0 ? -fontMetricsInt.top : (-fontMetricsInt.top)
						+ ((i4 - computeTextProperty.totalHeight) / 2);
				for (String str3 : computeTextProperty.lines) {
					// String str32;
					// canvas.drawText(str32, (float) computeX(newPaint, str32,
					// computeTextProperty.maxWidth, i2), (float) i5, newPaint);
					canvas.drawText(
							str3,
							(float) computeX(newPaint, str3,
									computeTextProperty.maxWidth, i2),
							(float) i5, newPaint);

					i5 += computeTextProperty.heightPerLine;
				}
				initNativeObject(createBitmap, "");
				return;
			}
		}
		String str4 = "";
		refactorString = str.replaceAll("begin@", "<").replaceAll("@end", ">");
		System.out.println("yujun content:" + refactorString);
		refactorString = refactorString(refactorString);
		int indexOf = refactorString.indexOf("<");
		int indexOf2 = refactorString.indexOf(">");
		if (indexOf != -1 && indexOf2 != -1) {
			Vector component = getComponent(refactorString);
			indexOf = 0;
			Vector vector = new Vector();
			Vector vector2 = new Vector();
			Iterator it = component.iterator();
			i6 = 0;
			int i7 = 0;
			int i8 = 0;
			int i9 = 0;
			indexOf2 = 0;
			while (it.hasNext()) {
				Component component2 = (Component) it.next();
				Paint newPaint2 = newPaint2(component2, str2, i);
				FontMetricsInt fontMetricsInt2 = newPaint2.getFontMetricsInt();
				int i10 = fontMetricsInt2.bottom - fontMetricsInt2.top;
				if (i8 < i10) {
					i9 = -fontMetricsInt2.top;
					i8 = i10;
				}
				LinkedList mySplitString = mySplitString(component2.mText, i4,
						i3, newPaint2);
				int i11 = i6;
				int i12 = 0;
				int i13 = i8;
				i6 = indexOf2;
				i8 = i7;
				i7 = i9;
				while (i12 < mySplitString.size() - 1) {
					i9 = ((MyStringLine) mySplitString.get(i12))
							.getTw(newPaint2);
					vector.add(new TextInfo(indexOf,
							(MyStringLine) mySplitString.get(i12), i9, i10, i8,
							newPaint2));
					i5 = i8 + i9;
					if (i11 < i5) {
						i11 = i5;
					}
					i8 = 0;
					vector2.add(Integer.valueOf(i6 + i7));
					i6 += i13;
					i7 = -fontMetricsInt2.top;
					indexOf++;
					i12++;
					i13 = i10;
				}
				i9 = ((MyStringLine) mySplitString
						.get(mySplitString.size() - 1)).getTw(newPaint2);
				vector.add(new TextInfo(indexOf, (MyStringLine) mySplitString
						.get(mySplitString.size() - 1), i9, i10, i8, newPaint2));
				i5 = i8 + i9;
				indexOf2 = i6;
				i9 = i7;
				i8 = i13;
				i6 = i11;
				i7 = i5;
			}
			vector2.add(Integer.valueOf(indexOf2 + i9));
			if (i6 >= i7) {
				i7 = i6;
			}
			Bitmap createBitmap2 = Bitmap.createBitmap(i7, indexOf2 + i8,
					Config.ARGB_8888);
			Canvas canvas2 = new Canvas(createBitmap2);
			Iterator it2 = vector.iterator();
			String str5 = str4;
			while (it2.hasNext()) {
				String str6;
				TextInfo textInfo = (TextInfo) it2.next();
				String str7 = textInfo.myStrLine.content;
				if (textInfo.myStrLine.emojiList.size() != 0) {
					str6 = str5;
					str5 = str7;
					for (indexOf2 = 0; indexOf2 < textInfo.myStrLine.emojiList
							.size(); indexOf2++) {
						refactorString = (String) textInfo.myStrLine.emojiList
								.get(indexOf2);
						System.out.println("yujun emoji:" + refactorString);
						String str32 = refactorString.substring(1);
						String substring = str5.substring(0,
								str5.indexOf(refactorString));
						str5 = str5.replaceFirst(refactorString, "    ");
						str6 = new StringBuilder(String.valueOf(str6))
								.append("|")
								.append(str32)
								.append(",")
								.append(String.valueOf(((int) Math
										.ceil(textInfo.paint
												.measureText(substring)))
										+ textInfo.x))
								.append(",")
								.append(String.valueOf(vector2
										.get(textInfo.lineIndex)))
								.append(",0,0").toString();
					}
				} else {
					str6 = str5;
					str5 = str7;
				}
				canvas2.drawText(str5, (float) textInfo.x,
						(float) ((Integer) vector2.get(textInfo.lineIndex))
								.intValue(), textInfo.paint);
				str5 = str6;
			}
			System.out.println("yujun java tagStr:" + str5);
			initNativeObject(createBitmap2, str5);
		}
	}

	private static Bitmap decodeResource(Resources resources, int i) {
		TypedValue typedValue = new TypedValue();
		resources.openRawResource(i, typedValue);
		Options options = new Options();
		options.inTargetDensity = typedValue.density;
		return BitmapFactory.decodeResource(resources, i, options);
	}

	private static LinkedList<String> divideStringWithMaxWidth(Paint paint,
			String str, int i) {
		int length = str.length();
		LinkedList<String> linkedList = new LinkedList();
		int i2 = 1;
		int i3 = 0;
		while (i2 <= length) {
			int lastIndexOf;
			int ceil = (int) Math.ceil((double) paint.measureText(str, i3, i2));
			if (ceil >= i) {
				lastIndexOf = str.substring(0, i2).lastIndexOf(" ");
				if (lastIndexOf != -1 && lastIndexOf > i3) {
					linkedList.add(str.substring(i3, lastIndexOf));
				} else if (ceil > i) {
					linkedList.add(str.substring(i3, i2 - 1));
					lastIndexOf = i2 - 1;
				} else {
					linkedList.add(str.substring(i3, i2));
					lastIndexOf = i2;
				}
				i2 = lastIndexOf;
			} else {
				lastIndexOf = i2;
				i2 = i3;
			}
			i3 = i2;
			i2 = lastIndexOf + 1;
		}
		if (i3 < length) {
			linkedList.add(str.substring(i3));
		}
		return linkedList;
	}

	private static Vector<Component> getComponent(String str) {
		String replace = str.replace("begin@", "<").replace("@end", ">");
		Vector<Component> vector = new Vector();
		Vector vector2 = new Vector();
		int indexOf = replace.indexOf("<");
		int i = 0;
		int indexOf2 = replace.indexOf(">");
		String str2 = replace;
		int i2 = indexOf;
		indexOf = indexOf2;
		while (i2 != -1) {
			int i3;
			String substring = str2.substring(i2, indexOf + 1);
			String substring2 = str2.substring(i2, indexOf);
			i2 = str2.indexOf(substring);
			String replaceFirst = i2 != -1 ? substring.indexOf("<p") == 0 ? str2
					.replaceFirst(substring, " ") : str2.replaceFirst(
					substring, "")
					: str2;
			if (substring2.indexOf("</") == 0) {
				substring2 = substring2.substring(2);
				if (i2 != -1) {
					for (int size = vector.size() - 1; size >= 0; size--) {
						Component component = (Component) vector.get(size);
						if (substring2 != null && component.mTag != null
								&& component.mTag.compareTo(substring2) == 0) {
							vector.add(new Component(replaceFirst.substring(i,
									i2), substring2, i2, vector2));
							i3 = i2;
							break;
						}
					}
					i3 = i;
				} else {
					i3 = i;
				}
				if (vector2.size() > 0) {
					vector2.removeElementAt(vector2.size() - 1);
				}
			} else {
				if (i != i2) {
					vector.add(new Component(replaceFirst.substring(i, i2),
							null, i2, vector2));
					i3 = i2;
				} else {
					i3 = i;
				}
				vector.add(new Component(null, substring2.substring(1,
						substring2.indexOf(32)), i2, vector2));
				vector2.add(substring2);
			}
			i2 = replaceFirst.indexOf("<");
			i = i3;
			str2 = replaceFirst;
			indexOf = replaceFirst.indexOf(">");
		}
		if (i < str2.length()) {
			vector.add(new Component(str2.substring(i), null, i, vector2));
		}
		Iterator it = vector.iterator();
		while (it.hasNext()) {
			str2 = ((Component) it.next()).mText;
			if (str2 == null || str2.compareTo("") == 0) {
				it.remove();
			}
		}
		return vector;
	}

	private static byte[] getPixels(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		byte[] bArr = new byte[((bitmap.getWidth() * bitmap.getHeight()) * 4)];
		Buffer wrap = ByteBuffer.wrap(bArr);
		// wrap.order(ByteOrder.nativeOrder());
		bitmap.copyPixelsToBuffer(wrap);
		return bArr;
	}

	private static void initNativeObject(Bitmap bitmap, String str) {
		byte[] pixels = getPixels(bitmap);
		if (pixels != null) {
			nativeInitBitmapDC(bitmap.getWidth(), bitmap.getHeight(), pixels,
					str);
		}
	}

	private static LinkedList<MyStringLine> myDivideStringWithMaxWidth(
			Paint paint, String str, int i) {
		int length = str.length();
		LinkedList linkedList = new LinkedList();
		linkedList = new LinkedList();
		LinkedList<MyStringLine> linkedList2 = new LinkedList();
		MyStringLine myStringLine = new MyStringLine();
		int i2 = 0;
		int i3 = 1;
		while (i3 <= length) {
			Boolean bool;
			int lastIndexOf;
			MyStringLine myStringLine2;
			int ceil = (int) Math.ceil((double) paint.measureText(str, i2, i3));
			Boolean valueOf = Boolean.valueOf(false);
			if (i3 < length - 2 && str.substring(i3 - 1, i3).equals("&")) {
				String substring = str.substring(i3 - 1, i3 + 2);
				if (substring.matches("&[12][0-9]")) {
					if (ceil >= i) {
						valueOf = Boolean.valueOf(true);
					} else {
						System.out.println(substring);
						myStringLine.emojiList.add(substring);
					}
					i3 += 2;
					bool = valueOf;
					if (ceil < i) {
						lastIndexOf = str.substring(0, i3).lastIndexOf(" ");
						if (lastIndexOf == -1 && lastIndexOf > i2) {
							myStringLine.content = str.substring(i2,
									lastIndexOf);
						} else if (ceil > i) {
							myStringLine.content = str.substring(i2, i3);
							lastIndexOf = i3;
						} else if (bool.booleanValue()) {
							myStringLine.content = str.substring(i2, i3 - 1);
							lastIndexOf = i3 - 1;
						} else {
							myStringLine.content = str.substring(i2,
									(i3 - 1) - 2);
							lastIndexOf = i3 - 3;
						}
						linkedList2.add(myStringLine);
						myStringLine2 = new MyStringLine();
						i3 = lastIndexOf;
					} else {
						lastIndexOf = i2;
						myStringLine2 = myStringLine;
					}
					i3++;
					myStringLine = myStringLine2;
					i2 = lastIndexOf;
				}
			}
			bool = valueOf;
			if (ceil < i) {
				lastIndexOf = i2;
				myStringLine2 = myStringLine;
			} else {
				lastIndexOf = str.substring(0, i3).lastIndexOf(" ");
				if (lastIndexOf == -1) {
				}
				if (ceil > i) {
					myStringLine.content = str.substring(i2, i3);
					lastIndexOf = i3;
				} else if (bool.booleanValue()) {
					myStringLine.content = str.substring(i2, i3 - 1);
					lastIndexOf = i3 - 1;
				} else {
					myStringLine.content = str.substring(i2, (i3 - 1) - 2);
					lastIndexOf = i3 - 3;
				}
				linkedList2.add(myStringLine);
				myStringLine2 = new MyStringLine();
				i3 = lastIndexOf;
			}
			i3++;
			myStringLine = myStringLine2;
			i2 = lastIndexOf;
		}
		if (i2 < length) {
			myStringLine.content = str.substring(i2);
			linkedList2.add(myStringLine);
		}
		for (i3 = 0; i3 < linkedList2.size(); i3++) {
			MyStringLine myStringLine3 = (MyStringLine) linkedList2.get(i3);
			System.out.println(myStringLine3.content + ",emoji size:"
					+ myStringLine3.emojiList.size());
		}
		return linkedList2;
	}

	private static LinkedList<MyStringLine> mySplitString(String str, int i,
			int i2, Paint paint) {
		String[] split = str.split("\\n");
		FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
		int ceil = i
				/ ((int) Math
						.ceil((double) (fontMetricsInt.bottom - fontMetricsInt.top)));
		LinkedList<MyStringLine> linkedList = new LinkedList();
		LinkedList linkedList2;
		int i3;
		int length;
		int i4;
		int i5;
		int i6;
		if (i2 != 0) {
			linkedList2 = new LinkedList();
			for (String str2 : split) {
				if (((int) Math.ceil((double) paint.measureText(str2))) > i2) {
					linkedList.addAll(myDivideStringWithMaxWidth(paint, str2,
							i2));
				} else {
					MyStringLine myStringLine = new MyStringLine();
					myStringLine.content = str2;
					int length2 = str2.length();
					i4 = 1;
					i5 = 0;
					while (i4 <= length2) {
						if (i5 + 1 < length2
								&& str2.substring(i5, i5 + 1).equals("&")
								&& i5 + 2 < length2) {
							String substring = str2.substring(i5, i5 + 3);
							if (substring.matches("&[12][0-9]")) {
								System.out.println(substring);
								myStringLine.emojiList.add(substring);
								i4 += 2;
							}
						}
						i6 = i4;
						i4++;
						i5 = i6;
					}
					linkedList.add(myStringLine);
				}
				if (ceil > 0 && linkedList.size() >= ceil) {
					break;
				}
			}
			if (ceil > 0 && linkedList.size() > ceil) {
				while (linkedList.size() > ceil) {
					linkedList.removeLast();
				}
			}
		} else if (i == 0 || split.length <= ceil) {
			i4 = 0;
			while (i4 < split.length) {
				MyStringLine myStringLine2 = new MyStringLine();
				myStringLine2.content = split[i4];
				length = split[i4].length();
				i5 = 1;
				i3 = 0;
				while (i5 <= length) {
					if (i3 + 1 < length
							&& split[i4].substring(i3, i3 + 1).equals("&")
							&& i3 + 2 < length) {
						String r4 = split[i4].substring(i3, i3 + 3);
						if (r4.matches("&[12][0-9]")) {
							System.out.println(r4);
							myStringLine2.emojiList.add(r4);
							i5 += 2;
						}
					}
					i6 = i5;
					i5++;
					i3 = i6;
				}
				linkedList.add(myStringLine2);
				i4++;
			}
		} else {
			linkedList2 = new LinkedList();
			i5 = 0;
			while (i5 < ceil) {
				MyStringLine myStringLine3 = new MyStringLine();
				myStringLine3.content = split[i5];
				int length3 = split[i5].length();
				i4 = 1;
				i3 = 0;
				while (i4 <= length3) {
					if (i3 + 1 < length3
							&& split[i5].substring(i3, i3 + 1).equals("&")
							&& i3 + 2 < length3) {
						String r4 = split[i5].substring(i3, i3 + 3);
						if (r4.matches("&[12][0-9]")) {
							System.out.println(r4);
							myStringLine3.emojiList.add(r4);
							i4 += 2;
						}
					}
					i6 = i4;
					i4++;
					i3 = i6;
				}
				linkedList.add(myStringLine3);
				i5++;
			}
		}
		return linkedList;
	}

	private static native void nativeInitBitmapDC(int i, int i2, byte[] bArr,
			String str);

	private static Paint newPaint(String str, int i, int i2) {
		Paint paint = new Paint();
		paint.setColor(-1);
		paint.setTextSize((float) i);
		paint.setAntiAlias(true);
		if (str.endsWith(".ttf")) {
			try {
				paint.setTypeface(Cocos2dxTypefaces.get(context, str));
			} catch (Exception e) {
				Log.e("Cocos2dxBitmap", "error to create ttf type face: " + str);
				paint.setTypeface(Typeface.create(str, 0));
			}
		} else {
			paint.setTypeface(Typeface.create(str, 0));
		}
		switch (i2) {
		case ALIGNLEFT /* 49 */:
			paint.setTextAlign(Align.LEFT);
			break;
		case ALIGNRIGHT /* 50 */:
			paint.setTextAlign(Align.RIGHT);
			break;
		case ALIGNCENTER /* 51 */:
			paint.setTextAlign(Align.CENTER);
			break;
		default:
			paint.setTextAlign(Align.LEFT);
			break;
		}
		return paint;
	}

	private static Paint newPaint2(Component component, String str, int i) {
		Paint newPaint = newPaint(str, i, ALIGNLEFT);
		Vector access$1 = component.mFontStyle;
		for (int i2 = 0; i2 < access$1.size(); i2++) {
			String[] split = ((String) access$1.get(i2)).split(" ");
			if ("<font".compareToIgnoreCase(split[0]) == 0) {
				for (int i3 = 1; i3 < split.length; i3++) {
					String[] split2 = split[i3].split("=");
					split2[1] = split2[1].replaceAll("'", "");
					if ("face".compareToIgnoreCase(split2[0]) == 0) {
						newPaint.setTypeface(Typeface.create(split2[1], 0));
					} else if ("size".compareToIgnoreCase(split2[0]) == 0) {
						newPaint.setTextSize((float) Integer
								.parseInt(split2[1]));
					} else if ("color".compareToIgnoreCase(split2[0]) == 0) {
						try {
							newPaint.setColor(Color.parseColor(split2[1]));
						} catch (Exception e) {
							Log.v("newPaint2::", "set font color err!!!");
							newPaint.setColor(-1);
						}
					}
				}
			} else if ("<a".compareToIgnoreCase(split[0]) == 0) {
				newPaint.setColor(-16776961);
				newPaint.setUnderlineText(true);
			} else if (!("<i".compareToIgnoreCase(split[0]) == 0
					|| "<b".compareToIgnoreCase(split[0]) == 0
					|| "<bi".compareToIgnoreCase(split[0]) == 0 || "<u"
						.compareToIgnoreCase(split[0]) == 0)) {
				"<p".compareToIgnoreCase(split[0]);
			}
		}
		return newPaint;
	}

	private static String refactorString(String string2) {
		if (string2.compareTo("") == 0) {
			return " ";
		}
		StringBuilder stringBuilder = new StringBuilder(string2);
		int n2 = 0;
		int n3 = stringBuilder.indexOf("\n");
		while (n3 != -1) {
			if (n3 == 0 || stringBuilder.charAt(n3 - 1) == '\n') {
				stringBuilder.insert(n2, " ");
				n2 = n3 + 2;
			} else {
				n2 = n3 + 1;
			}
			if (n2 > stringBuilder.length() || n3 == stringBuilder.length())
				break;
			n3 = stringBuilder.indexOf("\n", n2);
		}
		return stringBuilder.toString();
	}

	public static void setContext(Context context) {
		context = context;
	}

	private static String[] splitString(String str, int i, int i2, Paint paint) {
		int i3 = 0;
		String[] split = str.split("\\n");
		FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
		int ceil = i
				/ ((int) Math
						.ceil((double) (fontMetricsInt.bottom - fontMetricsInt.top)));
		LinkedList linkedList;
		if (i2 != 0) {
			linkedList = new LinkedList();
			int length = split.length;
			while (i3 < length) {
				String str2 = split[i3];
				if (((int) Math.ceil((double) paint.measureText(str2))) > i2) {
					linkedList
							.addAll(divideStringWithMaxWidth(paint, str2, i2));
				} else {
					linkedList.add(str2);
				}
				if (ceil > 0 && linkedList.size() >= ceil) {
					break;
				}
				i3++;
			}
			if (ceil > 0 && linkedList.size() > ceil) {
				while (linkedList.size() > ceil) {
					linkedList.removeLast();
				}
			}
			split = new String[linkedList.size()];
			linkedList.toArray(split);
			return split;
		} else if (i == 0 || split.length <= ceil) {
			return split;
		} else {
			linkedList = new LinkedList();
			while (i3 < ceil) {
				linkedList.add(split[i3]);
				i3++;
			}
			split = new String[linkedList.size()];
			linkedList.toArray(split);
			return split;
		}
	}
}
