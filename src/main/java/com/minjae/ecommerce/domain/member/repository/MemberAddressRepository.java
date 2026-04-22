package com.minjae.ecommerce.domain.member.repository;

import com.minjae.ecommerce.domain.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    List<MemberAddress> findAllByMember_MemberId(Long memberId);
    Optional<MemberAddress> findByMember_MemberIdAndIsDefault(Long memberId, Boolean isDefault);

    @Modifying
    @Query("UPDATE MemberAddress a SET a.isDefault = false WHERE a.member.memberId = :memberId")
    void resetDefaultByMemberId(Long memberId);
}
