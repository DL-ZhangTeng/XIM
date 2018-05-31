        loadingView = findViewById(R.id.loadingview);
        Log.e("LoadingView", String.valueOf(loadingView.isStart()));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                loadingView.stopAnimation();
                Log.e("LoadingView", String.valueOf(loadingView.isStart()));
            }
        }, 3000);