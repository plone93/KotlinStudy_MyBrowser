package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //웹뷰 기본설정
        webView.apply { 
            settings.javaScriptEnabled = true // 자바 스크립트 기능 활성화 (필수)
            webViewClient = WebViewClient() 
        }
        webView.loadUrl("http:www.google.com") // 웹뷰에 해당 페이지 로딩

        //컨텍스트 메뉴를 표시할 뷰 등록
        registerForContextMenu(webView)

        urlEditText.setOnEditorActionListener { _, actionId, _ -> 
            //urlEditText에 글자가 입력 될 때마다 호출, 반응한뷰,액션ID,이벤트 3가지이며 
            //사용하지않는 것은 _로 대치함
            if(actionId == EditorInfo.IME_ACTION_SEARCH) { // 검색버튼이 눌렸는지 확인
                webView.loadUrl(urlEditText.text.toString()) //검색창에 입력한 값을 웹뷰로 전달하여 로딩
                true
            } else {
                false
            }
        }
    }

    //뒤로가기 동작 재정의
    override fun onBackPressed() {
        super.onBackPressed()

        if(webView.canGoBack()) { // 웹뷰가 이전 페이지로 돌아 갈 수 있다면
            webView.goBack() //이전페이지로 이동
        } else { // 그렇지 않다면
            super.onBackPressed() // 액티비티 종료
        }
    }

    //액티비티에 옵션메뉴 표시하기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu) // 메뉴 리소스 지정, 메뉴 늘리기.늘리기(R.menu.main, menu)
        return true // true = 액티비티에 메뉴가 있다고 인식
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean { //메뉴 선택
        when(item?.itemId){ //스위치문 : item의 ID가 
            R.id.action_google, R.id.action_home -> { //action_google, action_home과 같다면
                webView.loadUrl("http://www.google.com") //구글로 이동
                return true
            }
            R.id.action_naver -> { //네이버
                webView.loadUrl("http://naver.com")
                return true
            }
            R.id.action_daum -> { //다음
                webView.loadUrl("http://daum.net")
                return true
            }
            R.id.action_call -> { //전화
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if(intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> { //문자
                //문자보내기
                sendSMS("031-123-4567", webView.url) // anko를 활용한 암시적 인텐트
                return true
            }
            R.id.action_email -> { //이메일
                //이메일
                email("test@example.com","좋은 사이트", webView.url)   // anko를 활용한 암시적 인텐트
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    //컨텍스트 메뉴 작성
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    //컨텍스트 메뉴 클릭 이벤트 처리
    override fun onContextItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.action_share -> {
                //페이지 공유

                return true
            }

            R.id.action_browser -> {
                //기본 웹브라우저 열기

                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
