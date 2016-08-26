package in.vaksys.customkeyborad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import in.vaksys.customkeyborad.emoji_pager.KeyboardView;
import in.vaksys.customkeyborad.sqlite.EmojiDataSource;
import in.vaksys.customkeyborad.sqlite.Recent;

public class EmojiKeyboardService extends InputMethodService implements android.inputmethodservice.KeyboardView.OnKeyboardActionListener {

    private ViewPager vp;
    private boolean caps = false;
    private static MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;
    private android.inputmethodservice.KeyboardView kv;
    private View view1;
    private View view2;
    View view;
    private Keyboard keyboard;
    private boolean unlocked;
    public static int keyboardHeight;
    private LinearLayout linearLayout;

    private static InputConnection ic;

    private static EmojiDataSource dataSource;
    private static ArrayList<Recent> recents;

    public EmojiKeyboardService() {
        super();

        if (Build.VERSION.SDK_INT >= 17) {
            enableHardwareAcceleration();
        }
    }

    @Override
    public View onCreateInputView() {


        view = getLayoutInflater().inflate(R.layout.keyboard, null);
        kv = (android.inputmethodservice.KeyboardView) view.findViewById(R.id.keyboard);
        keyboard = new Keyboard(this, R.xml.keypad_design);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);

        linearLayout = (LinearLayout) view.findViewById(R.id.footer_for_emoticons);
        Log.e("calling", "this");

//        dataSource = new EmojiDataSource(this);
//        dataSource.open();
//        recents = (ArrayList<Recent>) dataSource.getAllRecents();
//
//        for (int i = 0; i < recents.size(); i++) {
//            Log.v("recent_emojis", recents.get(i).id + " " + recents.get(i).count);
//        }
//
//        try {
//            PackageManager pm = getPackageManager();
//            pm.getPackageInfo("in.vaksys.customkeyborad", PackageManager.GET_ACTIVITIES);
//            unlocked = true;
//        } catch (Exception e) {
//            unlocked = checkUnlocked();
//        }
//
//        Display d = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        keyboardHeight = (int) (d.getHeight()/3.0);
//
//        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_main, null);
//
//        vp = (ViewPager) layout.findViewById(R.id.emojiKeyboard);
//
//        vp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, keyboardHeight));
//        tabs = (PagerSlidingTabStrip) layout.findViewById(R.id.tabs);
//        tabs.setIndicatorColor(getResources().getColor(R.color.pager_color));
//
//        adapter = new MyPagerAdapter(this, vp);
//        vp.setAdapter(adapter);
//
//        tabs.setViewPager(vp);
//
//        vp.setCurrentItem(1);
//
//        ImageButton delete = (ImageButton) layout.findViewById(R.id.delete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeText();
//            }
//        });
//
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//
//        if(sharedPrefs.getString("emoji_keyboard_color", "1").equals("2") || sharedPrefs.getString("emoji_keyboard_color", "1").equals("3")) {
//
//
//            view1 = layout.findViewById(R.id.view1);
//            view1.setVisibility(View.GONE);
//            view2 = layout.findViewById(R.id.view2);
//            view2.setVisibility(View.GONE);
//
//            if(sharedPrefs.getString("emoji_keyboard_color", "1").equals("2")) {
//                tabs.setBackgroundColor(getResources().getColor(android.R.color.black));
//                delete.setBackgroundColor(getResources().getColor(android.R.color.black));
//            } else {
//                tabs.setBackgroundColor(getResources().getColor(R.color.black));
//                delete.setBackgroundColor(getResources().getColor(R.color.black));
//            }
//        }
//
//        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        final IBinder token = this.getWindow().getWindow().getAttributes().token;
//
//        delete.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // switches to previous ime
//
//                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                vib.vibrate(25);
//
//                try {
//                    //imm.setInputMethod(token, LATIN);
//                    imm.switchToLastInputMethod(token);
//                } catch (Throwable t) { // java.lang.NoSuchMethodError if API_level<11
//                    Context context = getApplicationContext();
//                    CharSequence text = "Unfortunately input method switching isn't supported in your version of Android! You will have to do it manually :(";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                }
//
//                return false;
//            }
//        });
//
//        if (unlocked) {
//            return layout;
//        } else {
//            RelativeLayout unlockLayout = new RelativeLayout(this);
//            unlockLayout.addView(layout);
//
//            LinearLayout unlocker = (LinearLayout) getLayoutInflater().inflate(R.layout.trial_expired, null);
//            unlocker.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//            Button btn = (Button) unlocker.findViewById(R.id.upgrade);
//
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.klinker.android.emoji_keyboard&hl=en"));
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                }
//            });
//
//            unlockLayout.addView(unlocker, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
//            return unlockLayout;
//
//        }
        return view;
    }


    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        ic = getCurrentInputConnection();
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();

        try {
            dataSource.close();
        } catch (Exception e) {
            // something wrong with db?
        }
    }

    public static void addText(Context context, String emoji, int icon) {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.setSpan(new ImageSpan(context, R.drawable.ic_smiley_icon), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ic.commitText(builder, 1);
        builder.clear();
//       ic.commitText(emoji, 1);
//
//        for (int i = 0; i < recents.size(); i++) {
//            if (recents.get(i).text.equals(emoji)) {
//                dataSource.updateRecent(icon + "");
//                recents.get(i).count++;
//                return;
//            }
//        }
//
//        Recent recent = dataSource.createRecent(emoji, icon + "");
//
//        if (recent != null) {
//            recents.add(recent);
//        }
    }

    public static void removeText() {
        ic.deleteSurroundingText(1, 0);
    }

    public static void removeRecent(int position) {
        dataSource.deleteRecent(recents.get(position).id);
        recents.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

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
        Log.e("PRIMARY CODE", "onKey: " + primaryCode);
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

            case 5:
                getdata();

                break;

            case 66:
                keyboard = new Keyboard(this, R.xml.keypad_design);
                kv.setKeyboard(keyboard);
                break;
            case 47:
                // Toast.makeText(SimpleIME.this, "Hello Emoji", Toast.LENGTH_SHORT).show();

                keyboard = new Keyboard(this, R.xml.numberpad);
                kv.setKeyboard(keyboard);
             //   getdata();
                //

////
////                Intent  intent = new Intent(EmojiKeyboardService.this,EmojiKeyboardService.class);
////                startService(intent);
//
////                keyboard = new Keyboard(this, R.layout.keyboard_main);
////                kv.setKeyboard(keyboard);
////                kv.setShifted(true);
////                kv.setOnKeyboardActionListener(this);
//
//
//
//               /* kv.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
//                    @Override
//                    public void onPress(int primaryCode) {
//
//                    }
//
//                    @Override
//                    public void onRelease(int primaryCode) {
//
//                    }
//
//                    @Override
//                    public void onKey(int primaryCode, int[] keyCodes) {
//                        Log.e("New Kye", "onKey: " + primaryCode);
//
//                    }
//
//                    @Override
//                    public void onText(CharSequence text) {
//
//                    }
//
//                    @Override
//                    public void swipeLeft() {
//
//                    }
//
//                    @Override
//                    public void swipeRight() {
//
//                    }
//
//                    @Override
//                    public void swipeDown() {
//
//                    }
//
//                    @Override
//                    public void swipeUp() {
//
//                    }
//                });*/

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
            case 57433:
                Toast.makeText(EmojiKeyboardService.this, "click", Toast.LENGTH_SHORT).show();
                //  SimpleIME.addText(mEmojiTexts[primaryCode], sIconIds[primaryCode]);

                break;
            default:
                SpannableStringBuilder builder = new SpannableStringBuilder();

                builder.append((char) primaryCode);
                builder.setSpan(new ImageSpan(EmojiKeyboardService.this, R.drawable.ic_smiley_icon), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ic.commitText(builder, 1);

//                char code = (char) primaryCode;
//                if (Character.isLetter(code) && caps) {
//                    code = Character.toUpperCase(code);
//                }
//                ic.commitText(String.valueOf(code), 1);
        }
    }

    public View getdata() {

        kv.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        dataSource = new EmojiDataSource(this);
        dataSource.open();
        recents = (ArrayList<Recent>) dataSource.getAllRecents();

        for (int i = 0; i < recents.size(); i++) {
            Log.v("recent_emojis", recents.get(i).id + " " + recents.get(i).count);
        }

        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("in.vaksys.customkeyborad", PackageManager.GET_ACTIVITIES);
            unlocked = true;
        } catch (Exception e) {
            unlocked = checkUnlocked();
        }

        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        keyboardHeight = (int) (d.getHeight() / 3.0);

//       view = getLayoutInflater().inflate(R.layout.keyboard_main, null);
////        kv = (android.inputmethodservice.KeyboardView) view.findViewById(R.id.keyboard_view);
////        keyboard = new Keyboard(this, R.layout.keyboard_main);
////        kv.setKeyboard(keyboard);
//        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.keyboard_main, null);
////        keyboardView = (KeyboardView)view.getLayoutInflater().inflate(R.layout.keyboard, null);
////        keyboard = new Keyboard(this, R.layout.qwerty_keyboard);
////        keyboardView.setKeyboard(keyboard);
//
//
////        kv = (android.inputmethodservice.KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
//        kv = (android.inputmethodservice.KeyboardView) view.findViewById(R.id.keyboard);
//        keyboard = new Keyboard(this, R.layout.keylayout);
//        kv.setKeyboard(keyboard);

        vp = (ViewPager) view.findViewById(R.id.emojiKeyboard);

        vp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, keyboardHeight));
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
//        tabs.setIndicatorColor(getResources().getColor());
        // tabs.setIndicatorColor(getResources().getColor(R.color.pager_color));

        adapter = new MyPagerAdapter(this, vp);
        vp.setAdapter(adapter);

        tabs.setViewPager(vp);

        vp.setCurrentItem(0);

        ImageView abc_keypad = (ImageView) view.findViewById(R.id.img_keypad_main);
        ImageView abc_keypad1 = (ImageView) view.findViewById(R.id.img_keypad_main1);
        ImageView delete = (ImageView) view.findViewById(R.id.img_keypad_main1);

        abc_keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kv.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);


            }
        });


        abc_keypad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kv.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                // removeText();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeText();
            }
        });

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (sharedPrefs.getString("emoji_keyboard_color", "1").equals("2") || sharedPrefs.getString("emoji_keyboard_color", "1").equals("3")) {


            view1 = view.findViewById(R.id.view1);
            view1.setVisibility(View.GONE);
            view2 = view.findViewById(R.id.view2);
            view2.setVisibility(View.GONE);

            if (sharedPrefs.getString("emoji_keyboard_color", "1").equals("2")) {
                // tabs.setBackgroundColor(getResources().getColor(android.R.color.black));
                // delete.setBackgroundColor(getResources().getColor(android.R.color.black));
            } else {
                // tabs.setBackgroundColor(getResources().getColor(R.color.black));
                //  delete.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }

        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        final IBinder token = this.getWindow().getWindow().getAttributes().token;

//        delete.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // switches to previous ime
//
//                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                vib.vibrate(25);
//
//                try {
//                    //imm.setInputMethod(token, LATIN);
//                    imm.switchToLastInputMethod(token);
//                } catch (Throwable t) { // java.lang.NoSuchMethodError if API_level<11
//                    Context context = getApplicationContext();
//                    CharSequence text = "Unfortunately input method switching isn't supported in your version of Android! You will have to do it manually :(";
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, text, duration);
//                    toast.show();
//                }
//
//                return false;
//            }
//        });

        if (unlocked) {
            return view;
        } else {
            RelativeLayout unlockLayout = new RelativeLayout(this);
            unlockLayout.addView(view);

            LinearLayout unlocker = (LinearLayout) getLayoutInflater().inflate(R.layout.trial_expired, null);
            unlocker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            Button btn = (Button) unlocker.findViewById(R.id.upgrade);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.klinker.android.emoji_keyboard&hl=en"));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });

            unlockLayout.addView(unlocker, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
            return unlockLayout;

        }
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

    public class MyPagerAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        // private final String[] TITLES = {getString(R.string.recent), getString(R.string.people), getString(R.string.things), getString(R.string.nature), getString(R.string.places), getString(R.string.symbols)};
        private ViewPager pager;
        final int[] icons = new int[]{R.drawable.ic_clock, R.drawable.ic_smiley_icon, R.drawable.ic_bottle_icon, R.drawable.ic_dj_icon};
        private ArrayList<View> pages;

        public MyPagerAdapter(Context context, ViewPager pager) {
            super();

            this.pager = pager;
            this.pages = new ArrayList<View>();

            // pages.add(new KeyboardView(context, 0, recents).getView());
            pages.add(new KeyboardView(context, 1).getView());
            pages.add(new KeyboardView(context, 2).getView());
            pages.add(new KeyboardView(context, 3).getView());
            pages.add(new KeyboardView(context, 4).getView());
            // pages.add(new KeyboardView(context, 5).getView());
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            pager.addView(pages.get(position), position, keyboardHeight);
            return pages.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            pager.removeView(pages.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }


        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getPageIconResId(int position) {
            return icons[position];
        }
    }

    public static final int TRIAL_LENGTH = 5;

    public boolean checkUnlocked() {
        boolean unlocked = true;

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Android/data/com.klinker.android/");
        dir.mkdirs();
        File file = new File(dir, "keyboard_expires.txt");

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String s = reader.readLine();
                long date = Long.parseLong(s);

                if (Calendar.getInstance().getTimeInMillis() > date) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {

            }
        } else {
            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);

                pw.println(Calendar.getInstance().getTimeInMillis() + (TRIAL_LENGTH * 24 * 60 * 60 * 1000));

                pw.flush();
                pw.close();
                f.close();

                return true;
            } catch (Exception e) {

            }
        }

        return unlocked;
    }
}
