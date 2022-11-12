# 김영한님 스프링 MVC 스터디

- [서블릿](https://github.com/give928/study-spring-servlet)
- [타임리프](https://github.com/give928/study-spring-mvc-thymeleaf)
- [아이템 서비스](https://github.com/give928/study-spring-mvc-item-service)
- [메시지, 국제화](https://github.com/give928/study-spring-mvc-message)
- [검증](https://github.com/give928/study-spring-mvc-validation)
- [로그인, 필터, 스프링 인터셉터](https://github.com/give928/study-spring-mvc-login)
- [예외 처리와 오류 페이지](https://github.com/give928/study-spring-mvc-exception)
- [스프링 타입 컨버터](https://github.com/give928/study-spring-mvc-typeconverter)
- [파일 업로드](https://github.com/give928/study-spring-mvc-upload)

## 메시지, 국제화

### 메시지

기획자가 화면에 보이는 문구가 마음에 들지 않는다고, 상품명이라는 단어를 모두 상품이름으로 고쳐달라고 하면 어떻게 해야할까?

여러 화면에 보이는 상품명, 가격, 수량 등, label 에 있는 단어를 변경하려면 다음 화면들을 다 찾아가면서 모두 변경해야 한다. 지금처럼 화면 수가 적으면 문제가 되지 않지만 화면이 수십개 이상이라면 수십개의 파일을 모두 고쳐야 한다.

addForm.html , editForm.html , item.html , items.html

왜냐하면 해당 HTML 파일에 메시지가 하드코딩 되어 있기 때문이다.

이런 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.

예를 들어서 messages.properteis 라는 메시지 관리용 파일을 만들고
```
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```

각 HTML들은 다음과 같이 해당 데이터를 key 값으로 불러서 사용하는 것이다.
```html
<label for="itemName" th:text="#{item.itemName}"></label>
```

### 국제화
메시지에서 한 발 더 나가보자.

메시지에서 설명한 메시지 파일( messages.properteis )을 각 나라별로 별도로 관리하면 서비스를 국제화 할 수 있다.

예를 들어서 다음과 같이 2개의 파일을 만들어서 분류한다.

messages_en.properties
```
item=Item
item.id=Item ID
item.itemName=Item Name
item.price=price
item.quantity=quantity
```
messages_ko.properties
```
item=상품
item.id=상품 ID
item.itemName=상품명
item.price=가격
item.quantity=수량
```

영어를 사용하는 사람이면 messages_en.properties 를 사용하고,

한국어를 사용하는 사람이면 messages_ko.properties 를 사용하게 개발하면 된다.

이렇게 하면 사이트를 국제화 할 수 있다.

한국에서 접근한 것인지 영어에서 접근한 것인지는 인식하는 방법은 HTTP accept-language 해더 값을
사용하거나 사용자가 직접 언어를 선택하도록 하고, 쿠키 등을 사용해서 처리하면 된다.

메시지와 국제화 기능을 직접 구현할 수도 있겠지만, 스프링은 기본적인 메시지와 국제화 기능을 모두 제공한다. 그리고 타임리프도 스프링이 제공하는 메시지와 국제화 기능을 편리하게 통합해서 제공한다. 지금부터 스프링이 제공하는 메시지와 국제화 기능을 알아보자.

### 스프링 메시지 소스 설정

스프링은 기본적인 메시지 관리 기능을 제공한다.

메시지 관리 기능을 사용하려면 스프링이 제공하는 MessageSource 를 스프링 빈으로 등록하면 되는데, MessageSource 는 인터페이스이다. 따라서 구현체인 ResourceBundleMessageSource 를 스프링 빈으로 등록하면 된다.

직접 등록
```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("messages", "errors");
    messageSource.setDefaultEncoding("utf-8");
    return messageSource;
}
```

- basenames : 설정 파일의 이름을 지정한다.
  - messages 로 지정하면 messages.properties 파일을 읽어서 사용한다.
  - 추가로 국제화 기능을 적용하려면 messages_en.properties , messages_ko.properties 와 같이 파일명 마지막에 언어 정보를 주면된다. 만약 찾을 수 있는 국제화 파일이 없으면 messages.properties (언어정보가 없는 파일명)를 기본으로 사용한다.
  - 파일의 위치는 /resources/messages.properties 에 두면 된다.
  - 여러 파일을 한번에 지정할 수 있다. 여기서는 messages , errors 둘을 지정했다.
- defaultEncoding : 인코딩 정보를 지정한다. utf-8 을 사용하면 된다.

스프링 부트

스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록한다.

스프링 부트 메시지 소스 설정

스프링 부트를 사용하면 다음과 같이 메시지 소스를 설정할 수 있다.

application.properties
```
spring.messages.basename=messages,config.i18n.messages
```

스프링 부트 메시지 소스 기본 값
```
spring.messages.basename=messages
```

MessageSource 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 messages 라는 이름으로 기본 등록된다. 따라서 messages_en.properties , messages_ko.properties , messages.properties 파일만 등록하면 자동으로 인식된다.

메시지 파일 만들기

메시지 파일을 만들어보자. 국제화 테스트를 위해서 messages_en 파일도 추가하자.
- messages.properties :기본 값으로 사용(한글)
- messages_en.properties : 영어 국제화 사용

주의! 파일명은 massage가 아니라 messages다! 마지막 s에 주의하자
- /resources/messages.properties
- resources/messages_en.properties

### 스프링 메시지 소스 사용

MessageSource 인터페이스

MessageSource 인터페이스를 보면 코드를 포함한 일부 파라미터로 메시지를 읽어오는 기능을 제공한다.

가장 단순한 테스트는 메시지 코드로 hello 를 입력하고 나머지 값은 null 을 입력했다.
locale 정보가 없으면 basename 에서 설정한 기본 이름 메시지 파일을 조회한다. basename 으로 messages 를 지정 했으므로 messages.properties 파일에서 데이터 조회한다.

- 메시지가 없는 경우에는 NoSuchMessageException 이 발생한다.
- 메시지가 없어도 기본 메시지( defaultMessage )를 사용하면 기본 메시지가 반환된다.

다음 메시지의 {0} 부분은 매개변수를 전달해서 치환할 수 있다.

hello.name=안녕 {0} Spring 단어를 매개변수로 전달 안녕 Spring

국제화 파일 선택

locale 정보를 기반으로 국제화 파일을 선택한다.

- Locale 이 en_US 의 경우 messages_en_US messages_en messages 순서로 찾는다.
- Locale 에 맞추어 구체적인 것이 있으면 구체적인 것을 찾고, 없으면 디폴트를 찾는다고 이해하면 된다.

### 웹 애플리케이션에 메시지 적용하기

타임리프 메시지 적용

타임리프의 메시지 표현식 #{...} 를 사용하면 스프링의 메시지를 편리하게 조회할 수 있다. 예를 들어서 방금 등록한 상품이라는 이름을 조회하려면 #{label.item} 이라고 하면 된다.
```html
<div th:text="#{label.item}"></h2>
```

참고로 파라미터는 다음과 같이 사용할 수 있다.
```html
// hello.name=안녕 {0}
<p th:text="#{hello.name(${item.itemName})}"></p>
```

### 웹 애플리케이션에 국제화 적용하기

웹으로 확인하기

웹 브라우저의 언어 설정 값을 변경하면서 국제화 적용을 확인해보자. 

크롬 브라우저 설정 언어를 검색하고, 우선 순위를 변경하면 된다. 

우선순위를 영어로 변경하고 테스트해보자. 

웹 브라우저의 언어 설정 값을 변경하면 요청시 Accept-Language 의 값이 변경된다. 

Accept-Language 는 클라이언트가 서버에 기대하는 언어 정보를 담아서 요청하는 HTTP 요청 헤더이다.

### 스프링의 국제화 메시지 선택
앞서 MessageSource 테스트에서 보았듯이 메시지 기능은 Locale 정보를 알아야 언어를 선택할 수 있다.

결국 스프링도 Locale 정보를 알아야 언어를 선택할 수 있는데, 스프링은 언어 선택시 기본으로 Accept-Language 헤더의 값을 사용한다.

LocaleResolver

스프링은 Locale 선택 방식을 변경할 수 있도록 LocaleResolver 라는 인터페이스를 제공하는데, 스프링 부트는 기본으로 Accept-Language 를 활용하는 AcceptHeaderLocaleResolver 를 사용한다.

LocaleResolver 변경

만약 Locale 선택 방식을 변경하려면 LocaleResolver 의 구현체를 변경해서 쿠키나 세션 기반의 Locale 선택 기능을 사용할 수 있다. 예를 들어서 고객이 직접 Locale 을 선택하도록 하는 것이다. 관련해서 LocaleResolver 를 검색하면 수 많은 예제가 나오니 필요한 분들은 참고하자.
