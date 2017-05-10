$(document).ready(function(){

  var myPlaylist = new jPlayerPlaylist({
    jPlayer: "#jplayer_N",
    cssSelectorAncestor: "#jp_container_N"
  }, [
    {
      title:"心灵的故乡",
      artist:"丽江康康",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/66df83e2cb.mp3",
      poster: "images/m0.jpg"
    },
    {
      title:"我想和你虚度光阴",
      artist:"陈璧",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/bd1b473c3d.mp3",
      poster: "images/m0.jpg"
    },
    {
      title:"家乡",
      artist:"赵雷",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/765742856b.mp3",
      poster: "images/m0.jpg"
    },
    {
      title:"借我",
      artist:"谢春花",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/5b1b1b7e17.mp3",
      poster: "images/m0.jpg"
    },
    {
      title:"失语者",
      artist:"蔡健雅",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/d0530baa30.mp3",
      poster: "images/m0.jpg"
    },
    {
      title:"mertrain",
      artist:"Studios",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/88b756986a.mp3",
      poster: "Greyson Chan"
    },
    {
      title:"Sleepyhead",
      artist:"Galen Crew",
      mp3:"http://o6lf7x0ik.bkt.clouddn.com/a705849a78.mp3",
      poster: "images/m0.jpg"
    }
  ], {
    playlistOptions: {
      enableRemoveControls: true,
      autoPlay: false
    },
    swfPath: "js/jPlayer",
    supplied: "webmv, ogv, m4v, oga, mp3",
    smoothPlayBar: true,
    keyEnabled: true,
    audioFullScreen: false
  });
  
  $(document).on($.jPlayer.event.pause, myPlaylist.cssSelector.jPlayer,  function(){
    $('.musicbar').removeClass('animate');
    $('.jp-play-me').removeClass('active');
    $('.jp-play-me').parent('li').removeClass('active');
  });

  $(document).on($.jPlayer.event.play, myPlaylist.cssSelector.jPlayer,  function(){
    $('.musicbar').addClass('animate');
  });

  $(document).on('click', '.jp-play-me', function(e){
    e && e.preventDefault();
    var $this = $(e.target);
    if (!$this.is('a')) $this = $this.closest('a');

    $('.jp-play-me').not($this).removeClass('active');
    $('.jp-play-me').parent('li').not($this.parent('li')).removeClass('active');

    $this.toggleClass('active');
    $this.parent('li').toggleClass('active');
    if( !$this.hasClass('active') ){
      myPlaylist.pause();
    }else{
      var i = Math.floor(Math.random() * (1 + 7 - 1));
      myPlaylist.play(i);
    }
    
  });



  // video

  $("#jplayer_1").jPlayer({
    ready: function () {
      $(this).jPlayer("setMedia", {
        title: "Big Buck Bunny",
        m4v: "http://o6lfi4873.bkt.clouddn.com/1464620503374414.mp4",
        ogv: "http://flatfull.com/themes/assets/video/big_buck_bunny_trailer.ogv",
        webmv: "http://flatfull.com/themes/assets/video/big_buck_bunny_trailer.webm",
        poster: "images/m41.jpg"
      });
    },
    swfPath: "js",
    supplied: "m4v,webmv, ogv",
    size: {
      width: "100%",
      height: "auto",
      cssClass: "jp-video-360p"
    },
    globalVolume: true,
    smoothPlayBar: true,
    keyEnabled: true
  });

});