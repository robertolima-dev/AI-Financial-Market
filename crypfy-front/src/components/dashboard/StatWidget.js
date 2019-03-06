import React from 'react'
import StringUtils from '../../helpers/StringUtils'
import enhanceWithClickOutside from 'react-click-outside'

class StatWidget extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            items : this.props.items,
            opened: false
        }

    }

    handleClickOutside() {
        if(this.state.opened)
            this.setState({
                ...this.state,
                opened: false
            });
    }

    selectActive(idx) {
        const newItems = this.state.items.map((item,i) => {
            item.active = '';
            if(idx == i)
                item.active = 'active';
        })

        this.setState({...this.state,items:newItems});
    }

    render() {

        let activeItem = null;

        const items = this.state.items.map((item,i) => {

            if(item.active === 'active')
                activeItem = <div style={{padding:"20px 30px 20px 20px"}}>
                    <div className="float-left">
                        <h2>
                            {item.label}
                        </h2>
                    </div>
                    <div className="box-value float-right text-right">
                        <span className="title">
                            {this.props.name}
                        </span>
                        <span className="balance primary-color">
                            {item.value}
                            <span className="currency">{item.currency}</span>
                        </span>
                    </div>
                </div>

            return (
                <li onClick={this.selectActive.bind(this,i)} key={i} className={item.active}>
                    <a>
                        <span className="descript">{item.label}</span>
                        <span className="value">
                            {item.value}
                            <span className="currency">{item.currency}</span>
                        </span>
                    </a>
                </li>
            );
        })

        return (
            <div onClick={() => {(this.state.opened) ? this.setState({...this.state,opened:false}) : this.setState({...this.state,opened:true})}} className="balance-stats">
                {activeItem}
                <div className="clearfix"></div>
                <ul className={(this.state.opened) ? "details animated fadeIn active " : "details"}>
                    {items}
                </ul>
                {(this.state.opened) ? <span className="lnr lnr-chevron-up"></span> : <span className="lnr lnr-chevron-down"></span>}
                <div className="clearfix"></div>
            </div>
        );
    }
}

export default enhanceWithClickOutside(StatWidget);