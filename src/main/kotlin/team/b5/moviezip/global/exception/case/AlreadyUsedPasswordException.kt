package team.b5.moviezip.global.exception.case

class AlreadyUsedPasswordException :
    RuntimeException("이전에 사용한 패스워드를 재사용할 수 없습니다.")