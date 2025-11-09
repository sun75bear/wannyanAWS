
// debounce関数
function debounce(func, wait) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            func.apply(context, args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}


//ハンバーガーメニューをクリックした際の処理
$(function() {
	$('#menubar_hdr').click(function() {
		$(this).toggleClass('ham');

			if($(this).hasClass('ham')) {
				$('#menubar').addClass('d-b');
			} else {
				$('#menubar').removeClass('d-b');
			}

	});
});


// 同一ページへのリンクの場合に開閉メニューを閉じる処理
$(function() {
	$('#menubar a[href^="#"]').click(function() {
		$('#menubar').removeClass('d-b');
		$('#menubar_hdr').removeClass('ham');
	});
});


// スムーススクロール（※通常）
$(window).on("load resize", debounce(function() {
	var hash = location.hash;
	if(hash) {
		$('body,html').scrollTop(0);
		setTimeout(function() {
			var target = $(hash);
			var scroll = target.offset().top;
			$('body,html').animate({scrollTop:scroll},500);
		}, 5);
	}
	$('a[href^="#"]').click(function() {
		var href = $(this).attr('href');
		var target = href == '#' ? 0 : $(href).offset().top;
		$('body,html').animate({scrollTop:target},500);
		return false;
	});
}, 5));


//pagetop
$(function() {
    var scroll = $('.pagetop');
    var scrollShow = $('.pagetop-show');
        $(scroll).hide();
        $(window).scroll(function() {
            if($(this).scrollTop() >= 300) {
                $(scroll).fadeIn().addClass(scrollShow);
            } else {
                $(scroll).fadeOut().removeClass(scrollShow);
            }
        });
});


// 汎用開閉処理
$(function() {
	$('.openclose').next().hide();
	$('.openclose').click(function() {
		$(this).next().slideToggle();
		$('.openclose').not(this).next().slideUp();
	});
});


// 詳細ページのサムネイル切り替え
$(function() {
    // 初期表示: 各 .photo に対して、直後の .thumbnail の最初の画像を表示
    $(".photo").each(function() {
        var firstThumbnailSrc = $(this).next(".thumbnail").find("img:first").attr("src");
        var defaultImage = $("<img>").attr("src", firstThumbnailSrc);
        $(this).append(defaultImage);
    });

    // サムネイルがクリックされたときの動作
    $(".thumbnail img").click(function() {
        var imgSrc = $(this).attr("src");
        var newImage = $("<img>").attr("src", imgSrc).hide();

        // このサムネイルの直前の .photo 要素を取得
        var targetPhoto = $(this).parent(".thumbnail").prev(".photo");

        targetPhoto.find("img").fadeOut(400, function() {
            targetPhoto.empty().append(newImage);
            newImage.fadeIn(400);
        });
    });
});
