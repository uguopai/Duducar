//
//  HistoryTableViewCell.h
//  duducar
//
//  Created by macbook on 15/12/17.
//  Copyright © 2015年 guokrspace. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoryTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *startLabel;
@property (weak, nonatomic) IBOutlet UILabel *destLabel;
@property (weak, nonatomic) IBOutlet UILabel *orderStatusLabel;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;

@end
