package team.b5.moviezip.global.exception.case

class ModelNotFoundException(value: String) :
    RuntimeException("해당 ${value}을 찾을 수 없습니다.")