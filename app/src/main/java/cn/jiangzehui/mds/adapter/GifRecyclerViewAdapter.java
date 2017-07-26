package cn.jiangzehui.mds.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.jiangzehui.mds.R;
import cn.jiangzehui.mds.ShowPicActivity;
import cn.jiangzehui.mds.model.Gif;
import cn.jiangzehui.mds.util.T;

@SuppressWarnings("ALL")
public class GifRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflaters;
    private Context context;
    public ArrayList<Gif> list;
    private OnItemClickListener mOnItemClickListener;

    private boolean isFooterView = true;
    private final int NOFOOT = 1;
    private final int YESFOOT = 2;
    private View footView;

    public GifRecyclerViewAdapter(Context context, ArrayList<Gif> list) {
        isFooterView = false;
        inflaters = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    public GifRecyclerViewAdapter(Context context, ArrayList<Gif> list, View footView) {
        isFooterView = true;
        inflaters = LayoutInflater.from(context);
        this.list = list;
        this.footView = footView;
        this.context = context;
    }

    public void setList(ArrayList<Gif> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public int getListSize() {
        return this.list.size();
    }

    public void addList(ArrayList<Gif> lists) {
        if (lists.size() > 0) {
            for (Gif gif : lists) {
                this.list.add(gif);
            }
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setFooterView(boolean footerView) {
        isFooterView = footerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView) {
            if (position == (list.size())) {
                return YESFOOT;
            } else {
                return NOFOOT;
            }
        } else {
            return NOFOOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case YESFOOT:
                holder = new MyHolder_foot(footView);
                break;
            case NOFOOT:
                holder = new MyHolder(inflaters.inflate(R.layout.item_recyclerview_gif, parent, false));
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder) {
            MyHolder holder1 = (MyHolder) holder;
            holder1.setTitle(list.get(position).getTitle());
            holder1.setImg(list.get(position).getUrl(), position);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return isFooterView ? list.size() + 1 : list.size();
    }


    private class MyHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView iv;

        MyHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

        public void setTitle(String title) {
            if (null == tv_title) return;
            tv_title.setText(title + "\n");
        }

        void setImg(String imgUrl, final int position) {

            if (null == iv) return;
            if (!imgUrl.contains("http")) {
                imgUrl = "http://www.zbjuran.com" + imgUrl;
            }
            final WeakReference<ImageView> wr = new WeakReference<>(iv);
            final ImageView wr_iv = wr.get();
            if (imgUrl.contains("gif")) {
                boolean gif_auto_bool = false;
                if (!gif_auto_bool) {//是否自动加载git
                    final boolean[] bool = {true};

                    if (wr_iv != null) {
                        Glide.with(context).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_mr).into(wr_iv);
                        final String finalImgUrl = imgUrl;
                        wr_iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (finalImgUrl.contains("gif")) {
                                    if (bool[0]) {

                                        Glide.with(context).load(finalImgUrl).asGif().thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GifDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                if (isFirstResource) {
                                                    bool[0] = false;
                                                }
                                                return false;
                                            }
                                        }).placeholder(R.mipmap.ic_mr).into(wr_iv);

                                    } else {
                                        T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                                    }
                                } else {
                                    T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                                }
                            }
                        });
                    } else {
                        Glide.with(context).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_mr).into(iv);
                        final String finalImgUrl = imgUrl;
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (finalImgUrl.contains("gif")) {
                                    if (bool[0]) {
                                        bool[0] = false;
                                        Glide.with(context).load(finalImgUrl).asGif().thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GifDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                if (isFirstResource) {
                                                    bool[0] = false;
                                                }
                                                return false;
                                            }
                                        }).placeholder(R.mipmap.ic_mr).into(iv);
                                    } else {
                                        T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                                    }
                                } else {
                                    T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                                }
                            }
                        });
                    }
                } else {
                    Glide.with(context).load(imgUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_mr).into(iv);
                }
            } else {
                if (wr_iv != null) {
                    wr_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Intent intent = new Intent(context, ShowPicActivity.class);
                                intent.putExtra("url", list.get(position).getUrl());
                                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, wr_iv, "shareTransition").toBundle());
                            } else {
                                T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                            }
                        }
                    });
                    Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_mr).into(wr_iv);
                } else {
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Intent intent = new Intent(context, ShowPicActivity.class);
                                intent.putExtra("url", list.get(position).getUrl());
                                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, iv, "shareTransition").toBundle());
                            } else {
                                T.open(context, ShowPicActivity.class, "url", list.get(position).getUrl());
                            }
                        }
                    });
                    Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_mr).into(iv);
                }
            }
        }
    }

    private class MyHolder_foot extends RecyclerView.ViewHolder {
        MyHolder_foot(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
