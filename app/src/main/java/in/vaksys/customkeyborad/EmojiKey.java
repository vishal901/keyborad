package in.vaksys.customkeyborad;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EmojiKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, EmoticonsGridAdapter.KeyClickListener, KeyEvent.Callback {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;
    private LinearLayout emojiKeyboard, parent;
    private View popUpWindow;
    private ArrayList<Spanned> chats;
    private int keyboardHeight;
    private boolean isKeyBoardVisible;
    private Bitmap[] emoticons;
    //private PopupWindow popupWindow;
    private static final int NO_OF_EMOTICONS = 54;

    InputStream ims = null;
    View view;

    @Override
    public View onCreateInputView() {
        view = getLayoutInflater().inflate(R.layout.keyboard, null);
        kv = (KeyboardView) view.findViewById(R.id.keyboard);
        emojiKeyboard = (LinearLayout) view.findViewById(R.id.footer_for_emoticons);
        parent = (LinearLayout) view.findViewById(R.id.listValue);
        popUpWindow = getLayoutInflater().inflate(R.layout.emoticons_popup, null);

        keyboard = new Keyboard(this, R.xml.emoji_a2);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return view;
    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            case -11:
                keyboard = new Keyboard(this, R.xml.qwerty);
                kv.setKeyboard(keyboard);
                break;

            case 47:
                // Toast.makeText(SimpleIME.this, "Hello Emoji", Toast.LENGTH_SHORT).show();

                keyboard = new Keyboard(this, R.xml.emoji_a2);
                kv.setKeyboard(keyboard);
                kv.setShifted(true);
                kv.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
                    @Override
                    public void onPress(int primaryCode) {

                    }

                    @Override
                    public void onRelease(int primaryCode) {

                    }

                    @Override
                    public void onKey(int primaryCode, int[] keyCodes) {
                        InputConnection ic = getCurrentInputConnection();
                        playClick(primaryCode);
                        switch (primaryCode) {
                            case Keyboard.KEYCODE_DELETE:
                                ic.deleteSurroundingText(1, 0);
                                break;
                            case Keyboard.KEYCODE_SHIFT:
                                caps = !caps;
                                keyboard.setShifted(caps);
                                kv.invalidateAllKeys();
                                break;
                        }
                        //Toast.makeText(getApplicationContext(), "Hello Keyboard", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onText(CharSequence text) {

                    }

                    @Override
                    public void swipeLeft() {

                    }

                    @Override
                    public void swipeRight() {

                    }

                    @Override
                    public void swipeDown() {

                    }

                    @Override
                    public void swipeUp() {

                    }
                });

//                if (!popupWindow.isShowing()) {
//
//                    popupWindow.setHeight((int) (keyboardHeight));
//
//                    if (isKeyBoardVisible) {
//                        emojiKeyboard.setVisibility(LinearLayout.GONE);
//                    } else {
//                        emojiKeyboard.setVisibility(LinearLayout.VISIBLE);
//                    }
//                    popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//
//                } else {
//                    popupWindow.dismiss();
//                }
//                kv.setVisibility(View.GONE);
//                emojiKeyboard.setVisibility(LinearLayout.VISIBLE);
//
//                ImageView imageView = (ImageView) view.findViewById(R.id.img);
//
//                // get input stream
//
//                try {
//                    ims = getAssets().open("1.png");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                // load image as Drawable
//                Drawable d = Drawable.createFromStream(ims, null);
//                // set image to ImageView
//                imageView.setImageDrawable(d);
//
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                    }
//                });


//                readEmoticons();
//                enablePopUpView();
//                checkKeyboardHeight(parent);
                //   kv.setVisibility(View.GONE);

//                if (isKeyBoardVisible) {
//                    emojiKeyboard.setVisibility(LinearLayout.GONE);
//                } else {

                //  }

                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    private void readEmoticons() {

        emoticons = new Bitmap[NO_OF_EMOTICONS];
        for (short i = 0; i < NO_OF_EMOTICONS; i++) {
            emoticons[i] = getImage((i + 1) + ".png");

        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (popupWindow.isShowing()) {
//            popupWindow.dismiss();
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }


    int previousHeightDiffrence = 0;

    private void checkKeyboardHeight(final View parentLayout) {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight = parentLayout.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);

                        if (previousHeightDiffrence - heightDifference > 50) {
                            //popupWindow.dismiss();
                        }

                        previousHeightDiffrence = heightDifference;
                        if (heightDifference > 100) {

                            isKeyBoardVisible = true;
                            changeKeyboardHeight(heightDifference);

                        } else {

                            isKeyBoardVisible = false;

                        }

                    }
                });

    }

    private void changeKeyboardHeight(int height) {

        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, keyboardHeight);
            emojiKeyboard.setLayoutParams(params);
        }

    }

    private void enablePopUpView() {

        Log.e("1", "1");

        ViewPager pager = (ViewPager) popUpWindow.findViewById(R.id.emoticons_pager);
        pager.setOffscreenPageLimit(3);

        ArrayList<String> paths = new ArrayList<String>();

        for (short i = 1; i <= NO_OF_EMOTICONS; i++) {
            paths.add(i + ".png");

        }
        Log.e("2", "2");
        EmoticonsPagerAdapter adapter = new EmoticonsPagerAdapter(getApplicationContext(), paths, this);
        pager.setAdapter(adapter);

        // Creating a pop window for emoticons keyboard
        //popupWindow = new PopupWindow(popUpWindow, LinearLayout.LayoutParams.MATCH_PARENT,
        //(int) keyboardHeight, false);

        TextView backSpace = (TextView) popUpWindow.findViewById(R.id.back);
        backSpace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                // content.dispatchKeyEvent(event);

                Log.e("key on click", "click");
            }
        });

        //popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

//            @Override
//            public void onDismiss() {
//                emojiKeyboard.setVisibility(LinearLayout.GONE);
//            }
//        });
    }

    private Bitmap getImage(String path) {
        AssetManager mngr = getAssets();
        InputStream in = null;
        try {
            in = mngr.open("emoticons/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        return temp;
    }

    @Override
    public void keyClickedIndex(final String index) {

        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                StringTokenizer st = new StringTokenizer(index, ".");
                Drawable d = new BitmapDrawable(getResources(), emoticons[Integer.parseInt(st.nextToken()) - 1]);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };

        Spanned cs = Html.fromHtml("<img src ='" + index + "'/>", imageGetter, null);


    }
}