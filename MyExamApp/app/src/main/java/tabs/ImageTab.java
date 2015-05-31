package tabs;

/**
 * Created by Tony on 2015/5/30.
 */
public class ImageTab {
    private String mTitle;
    private String mTime;
    private int mImageId;
    private String mLike;
    private String mNegative;
    private String mTucao;

    public ImageTab(String mTitle, String mTime, int mImageId, String mLike, String mNegative, String mTucao) {
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mImageId = mImageId;
        this.mLike = mLike;
        this.mNegative = mNegative;
        this.mTucao = mTucao;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getmImageId() {
        return mImageId;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getmLike() {
        return mLike;
    }

    public void setmLike(String mLike) {
        this.mLike = mLike;
    }

    public String getmTucao() {
        return mTucao;
    }

    public void setmTucao(String mTucao) {
        this.mTucao = mTucao;
    }

    public String getmNegative() {
        return mNegative;
    }

    public void setmNegative(String mNegative) {
        this.mNegative = mNegative;
    }
}

