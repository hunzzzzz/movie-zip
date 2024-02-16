package team.b5.moviezip.global.exception.case

class DuplicatedLikeException(value: String) :
    RuntimeException("중복해서 ${value}를 누를 수 없습니다.")